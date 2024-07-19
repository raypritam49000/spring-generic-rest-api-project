package com.generic.rest.api.project.service.impl;

import com.generic.rest.api.project.dto.*;
import com.generic.rest.api.project.exception.ResourceConflictException;
import com.generic.rest.api.project.exception.ResourceNotFoundException;
import com.generic.rest.api.project.jsonwebtoken.AuthTokenDetailsDTO;
import com.generic.rest.api.project.jsonwebtoken.JsonWebTokenUtility;
import com.generic.rest.api.project.mappers.PermissionsMapper;
import com.generic.rest.api.project.mappers.RolesMapper;
import com.generic.rest.api.project.mappers.UserEntityMapper;
import com.generic.rest.api.project.mappers.UserEntityMapperImpl;
import com.generic.rest.api.project.model.Permission;
import com.generic.rest.api.project.model.Role;
import com.generic.rest.api.project.model.UserEntity;
import com.generic.rest.api.project.pages.UserEntityPageDTO;
import com.generic.rest.api.project.repository.RoleRepository;
import com.generic.rest.api.project.repository.UserEntityRepository;
import com.generic.rest.api.project.search.EntityUtilities;
import com.generic.rest.api.project.service.RoleService;
import com.generic.rest.api.project.service.UserEntityService;
import com.generic.rest.api.project.utility.PermissionUtilities;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private final UserEntityRepository userRepository;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserEntityServiceImpl(UserEntityRepository userRepository, Environment env, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.env = env;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserEntityDTO createUser(String auth, UserEntityDTO userDTO) {

        var authTokenDetails = JsonWebTokenUtility.parseAndValidate(auth);

        userDTO.setUsername(StringUtils.trimToNull(userDTO.getUsername()));
        userDTO.setEmail(StringUtils.trimToNull(userDTO.getEmail()));

        UserEntity existingUser = userRepository.findByUsernameAndDeletedFalse(userDTO.getUsername());

        if (existingUser != null) {
            throw new ResourceConflictException("User already register with given username : " + userDTO.getUsername());
        }

        List<Role> roles = roleRepository.findAllByNameIn(userDTO.getRoles());

        if (StringUtils.isNotEmpty(userDTO.getPassword())) {
            userDTO.setCredentialsNonExpired(true);
            userDTO.setLastPasswordChangeDate(new Date());
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        UserEntity user = new UserEntity(userDTO.getUsername(), userDTO.getEmail(), userDTO.getTenant(), userDTO.getPassword(), roles, userDTO.isEnabled(), true, true, true, userDTO.getCustomerLevel());

        assert authTokenDetails != null;
        user.setCreatedBy(authTokenDetails.getUsername());
        user.setCreationDate(new Date());

        return UserEntityMapper.INSTANCE.toDto(userRepository.save(user));

    }


    @Override
    public AuthTokenDTO authenticateUser(String emailOrUsername, String password, String lockIp, String userZoneId) {
        UserEntity user = userRepository.findByUsernameOrEmailAndDeletedFalse(emailOrUsername, emailOrUsername).orElseThrow(() -> new ResourceNotFoundException("User not found with given username : " + emailOrUsername));

        if (!
                passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        var permissions = findAllPermissionsForUser(user.getId());
        var roles = findAllRolesForUserInternal(user.getId());

        List<String> permissionAbbreviations = permissions.stream().map(PermissionDTO::getAbbr).distinct().collect(toList());
        List<String> roleNames = roles.stream().map(RoleDTO::getName).distinct().collect(toList());

        AuthTokenDetailsDTO authTokenDetails = new AuthTokenDetailsDTO();
        authTokenDetails.setUserId(user.getId());
        authTokenDetails.setUsername(user.getUsername());
        authTokenDetails.setEmail(user.getEmail());
        authTokenDetails.setLogIp(lockIp);
        authTokenDetails.setCustomerLevel(user.getCustomerLevel());
        authTokenDetails.setTenant(user.getTenant());
        authTokenDetails.setEnvironment(env.getActiveProfiles()[0]);
        authTokenDetails.setRoleNames(roleNames);
        authTokenDetails.setGrantedAuthorities(permissionAbbreviations);
        authTokenDetails.setExpirationDate(buildExpirationDate());
        authTokenDetails.setLastPasswordChangeDate(user.getLastPasswordChangeDate());
        authTokenDetails.setUserZoneId(userZoneId);

        String jwt = JsonWebTokenUtility.createJsonWebToken(authTokenDetails);

        AuthTokenDTO authToken = new AuthTokenDTO();
        authToken.setToken(jwt);

        return authToken;
    }

    @Override
    public void deleteUser(String auth, String id) {
        var authTokenDetails = JsonWebTokenUtility.parseAndValidate(auth);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        user.setDeleted(true);
        assert authTokenDetails != null;
        user.setModifiedBy(authTokenDetails.getUsername());
        userRepository.save(user);
    }

    @Override
    public UserEntityDTO updateUser(String auth, String id, UserEntityDTO userDTO) {
        var authTokenDetails = JsonWebTokenUtility.parseAndValidate(auth);
        assert authTokenDetails != null;

        String trimmedUsername = StringUtils.trimToNull(userDTO.getUsername());
        String trimmedEmail = StringUtils.trimToNull(userDTO.getEmail());

        userDTO.setUsername(trimmedUsername);
        userDTO.setEmail(trimmedEmail);

        var user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));

        if (!user.getUsername().equalsIgnoreCase(trimmedUsername) || !user.getEmail().equalsIgnoreCase(trimmedEmail)) {
            userRepository.findByUsernameOrEmailAndDeletedFalse(trimmedUsername, trimmedEmail).ifPresent(matchingUser -> {
                throw new ResourceConflictException("User already register with given username : " + matchingUser.getUsername());
            });
        }

        updateUserDetails(user, userDTO, authTokenDetails.getUsername());
        return UserEntityMapper.INSTANCE.toDto(userRepository.save(user));
    }

    @Override
    public UserEntityPageDTO findAllUsers(String auth, int page, int size, String sort, String sortOrder) {
        if (StringUtils.isEmpty(sort)) {
            sort = "username";
        }

        UserEntityPageDTO pageableDTO = new UserEntityPageDTO();
        Pageable paging;
        if (sortOrder != null && sortOrder.equals("ASCENDING"))
            paging = PageRequest.of(page, size, Sort.by(sort).ascending());
        else paging = PageRequest.of(page, size, Sort.by(sort).descending());

        Page<UserEntity> pagedResult = null;

        if (PermissionUtilities.isSys(auth)) pagedResult = userRepository.findAllByDeletedFalse(paging);
        else if (PermissionUtilities.isTenant(auth))
            pagedResult = userRepository.findAllByTenantAndDeletedFalse(Objects.requireNonNull(JsonWebTokenUtility.parseAndValidate(auth)).getTenant(), paging);

        assert pagedResult != null;
        return EntityUtilities.transferToPageDTO(pagedResult, pageableDTO, UserEntityMapper.INSTANCE, sort, sortOrder, "Users");
    }

    @Override
    public UserEntityDTO findUserById(String auth, String id) {
        return UserEntityMapper.INSTANCE.toDto(userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id)));
    }

    @Override
    public UserEntityDTO findUserByUsername(String auth, String username) {
        UserEntity user = userRepository.findByUsernameAndDeletedFalse(username);
        if (ObjectUtils.isEmpty(user))
            throw new ResourceNotFoundException("User not found with given username: " + username);
        return UserEntityMapper.INSTANCE.toDto(user);
    }

    @Override
    public UserEntityDTO findUserByEmailInternal(String email) {
        return UserEntityMapper.INSTANCE.toDto(userRepository.findByEmailAndDeletedFalse(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email: " + email)));
    }

    @Override
    public UserEntityDTO findUserByEmail(String auth, String email) {
        return UserEntityMapper.INSTANCE.toDto(userRepository.findByEmailAndDeletedFalse(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email: " + email)));
    }

    @Override
    public List<RoleDTO> findAllRolesForUser(String auth, String id) {
        var user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        return RolesMapper.INSTANCE.toDtoList(user.getRoles());
    }

    @Override
    public UserEntityDTO findByUsernameOrEmailAndDeletedFalse(String auth, String username, String email) {
        UserEntity user = userRepository.findByUsernameOrEmailAndDeletedFalse(username, email).orElseThrow(() -> new ResourceNotFoundException("User not found with given username : " + username));
        return UserEntityMapper.INSTANCE.toDto(user);
    }

    private void updateUserDetails(UserEntity user, UserEntityDTO userDTO, String modifiedBy) {
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.isEnabled());
        user.setAccountNonLocked(userDTO.isAccountNonLocked());
        user.setAccountNonExpired(userDTO.isAccountNonExpired());
        user.setCredentialsNonExpired(userDTO.isCredentialsNonExpired());
        user.setInTraining(userDTO.isInTraining());
        user.setModifiedBy(modifiedBy);
        user.setModifiedDate(new Date());
        user.setRoles(roleRepository.findAllByNameIn(userDTO.getRoles()));
    }


    private List<PermissionDTO> findAllPermissionsForUser(String id) {
        UserEntity user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        List<Permission> permissions = new ArrayList<>();
        for (Role role : user.getRoles()) {
            permissions.addAll(role.getPermissions());
        }
        return PermissionsMapper.INSTANCE.toDtoList(permissions);
    }

    private List<RoleDTO> findAllRolesForUserInternal(String id) {
        UserEntity user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        List<Role> roles = user.getRoles();
        return RolesMapper.INSTANCE.toDtoList(roles);
    }

    private Date buildExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 12);
        return calendar.getTime();
    }
}
