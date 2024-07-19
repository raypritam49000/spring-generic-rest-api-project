package com.generic.rest.api.project;

import com.generic.rest.api.project.enumeration.CustomerLevel;
import com.generic.rest.api.project.model.Permission;
import com.generic.rest.api.project.model.Role;
import com.generic.rest.api.project.model.UserEntity;
import com.generic.rest.api.project.repository.PermissionRepository;
import com.generic.rest.api.project.repository.RoleRepository;
import com.generic.rest.api.project.repository.UserEntityRepository;
import com.generic.rest.api.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringGenericRestApiProjectApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(SpringGenericRestApiProjectApplication.class, args);
    }

    @Transactional
    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            // Create user
            List<Role> roles = List.of(new Role("ADMIN", Set.of(new Permission("READ", "Read Permission"), new Permission("WRITE", "Write Permission")), 1));
           // UserEntity userEntity = userEntityRepository.save(new UserEntity("admin", "john.doe@example.com", "hashed_password", BCrypt.hashpw("admin",BCrypt.gensalt()), roles, true, true, true, true, CustomerLevel.SYS));
           // System.out.println(userEntity);

            System.out.println(Arrays.toString(env.getActiveProfiles()));

        };
    }
}
