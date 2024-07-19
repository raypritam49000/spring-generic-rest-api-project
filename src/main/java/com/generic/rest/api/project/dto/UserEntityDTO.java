package com.generic.rest.api.project.dto;

import com.generic.rest.api.project.enumeration.CustomerLevel;
import com.generic.rest.api.project.search.dto.base.TenantAuditableBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityDTO extends TenantAuditableBaseDTO {
        @NotNull
        @NotEmpty(message = "Required")
        private String username;
        @NotNull
        @NotEmpty(message = "Required")
        @Email
        private String email;
        private String password;
        @NotNull
        @NotEmpty(message = "Required")
        private List<String> roles;
        private boolean enabled;
        private boolean accountNonExpired;
        private boolean credentialsNonExpired;
        private boolean accountNonLocked;
        private int failedAttempt;
        public Date lockTime;
        public Date lastLoginDate;
        private Date lastPasswordChangeDate;
        private boolean inTraining;
        private CustomerLevel customerLevel;
}
