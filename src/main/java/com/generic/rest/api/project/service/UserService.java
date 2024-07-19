package com.generic.rest.api.project.service;

import com.generic.rest.api.project.dto.UserSearchParameters;
import com.generic.rest.api.project.pages.UserPageDTO;

public interface UserService {
    public UserPageDTO searchEmployee(UserSearchParameters userSearchParameters);
}
