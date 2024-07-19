package com.generic.rest.api.project.utility;

import com.generic.rest.api.project.jsonwebtoken.AuthTokenDetailsDTO;
import com.generic.rest.api.project.jsonwebtoken.JsonWebTokenUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Component
public class PermissionUtilities {

    public static boolean isSys(String auth) {
        List<String> roleNames = Objects.requireNonNull(JsonWebTokenUtility.parseAndValidate(auth)).getRoleNames();
        for (String role : roleNames) {
            if (role.contains("SYS")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTenant(String auth) {
        List<String> roleNames = Objects.requireNonNull(JsonWebTokenUtility.parseAndValidate(auth)).getRoleNames();
        for (String role : roleNames) {
            if (role.contains("TENANT")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSysOrTenant(String auth) {
        List<String> roleNames = Objects.requireNonNull(JsonWebTokenUtility.parseAndValidate(auth)).getRoleNames();
        for (String role : roleNames) {
            if (role.contains("SYS") || role.contains("TENANT")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSysOrAllowedTenant(String auth, String tenant) {
        List<String> roleNames = Objects.requireNonNull(JsonWebTokenUtility.parseAndValidate(auth)).getRoleNames();
        for (String role : roleNames) {
            if (roleNames.contains("SYS") || (roleNames.contains("TENANT") && Objects.requireNonNull(JsonWebTokenUtility.parseAndValidate(auth)).getTenant().equals(tenant))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllowed(String auth, String tenant, String accountId) {
        AuthTokenDetailsDTO authTokenDetailsDTO = JsonWebTokenUtility.parseAndValidate(auth);
        assert authTokenDetailsDTO != null;
        for (String roleName : authTokenDetailsDTO.getRoleNames()) {
            if (roleName.contains("SYS")) {
                return true;
            }
            if (roleName.contains("TENANT")) {
                return true;
            }
            if (roleName.contains("ACCOUNT")) {
                return true;
            }
        }
        return false;
    }

    public static String getBasicAuthToken(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }

        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}