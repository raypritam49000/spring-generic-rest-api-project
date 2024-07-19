package com.generic.rest.api.project.model;

import com.generic.rest.api.project.enumeration.CustomerLevel;
import com.generic.rest.api.project.search.entity.TenantAuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends TenantAuditableBaseEntity {
    @Column(unique = true, nullable = false)
    private String username;
    private String passwordHash;
    private String email;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "user_role_tb",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

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

    public UserEntity(String username, String email, String tenant, String passwordHash, List<Role> roles, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,CustomerLevel customerLevel) {
        setUsername(username);
        setEmail(email);
        setPasswordHash(passwordHash);
        setTenant(tenant);
        setRoles(roles);
        setEnabled(enabled);
        setAccountNonExpired(accountNonExpired);
        setCredentialsNonExpired(credentialsNonExpired);
        setAccountNonLocked(accountNonLocked);
        setCustomerLevel(customerLevel);
    }

}
