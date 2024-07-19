package com.generic.rest.api.project.service.impl;

import com.generic.rest.api.project.dto.UserSearchParameters;
import com.generic.rest.api.project.mappers.UserMapper;
import com.generic.rest.api.project.model.User;
import com.generic.rest.api.project.pages.UserPageDTO;
import com.generic.rest.api.project.search.GenericEntitySpecifications;
import com.generic.rest.api.project.search.SearchBuilder;
import com.generic.rest.api.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public UserPageDTO searchEmployee(UserSearchParameters userSearchParameters) {
        UserPageDTO pageableDTO = new UserPageDTO();
        return getComputedSearchBuilder(userSearchParameters).getAsPageDTO(pageableDTO, UserMapper.INSTANCE, userSearchParameters.getSort(), userSearchParameters.getSortOrder(), "Users");
    }

    public SearchBuilder<User> getComputedSearchBuilder(UserSearchParameters userSearchParameters) {
        SearchBuilder<User> searchBuilder = new SearchBuilder<>(userSearchParameters.getPageNumber(), userSearchParameters.getPageSize(), entityManager, User.class);
        searchBuilder.applyAuditableSearchParameters(userSearchParameters);
        searchBuilder.setSortCriterion(userSearchParameters.getSortOrder(), userSearchParameters.getSort());
        searchBuilder.addSearchFilter(GenericEntitySpecifications.matchBoolean(false, "deleted"));
        searchBuilder.addSearchFilter(GenericEntitySpecifications.exactMatchText(userSearchParameters.getEmail(), "email"));
        searchBuilder.addSearchFilter(GenericEntitySpecifications.exactMatchText(userSearchParameters.getUsername(), "username"));
        searchBuilder.addSearchFilter(GenericEntitySpecifications.exactMatchText(userSearchParameters.getPassword(), "password"));
        searchBuilder.addSearchFilter(GenericEntitySpecifications.isDateInRange(userSearchParameters.getCreationDateRange(), "creationDate"));
        searchBuilder.addSearchFilter(GenericEntitySpecifications.matchObjectIn(userSearchParameters.getGenderType(), "genderType"));
        searchBuilder.addSearchFilter(GenericEntitySpecifications.matchObjectIn(userSearchParameters.getStatus(), "status"));
        searchBuilder.addSearchFilter(GenericEntitySpecifications.matchObjectIn(userSearchParameters.getType(), "type"));

        return searchBuilder;
    }
}
