package com.generic.rest.api.project.model;

import com.generic.rest.api.project.enumeration.GenderType;
import com.generic.rest.api.project.enumeration.Status;
import com.generic.rest.api.project.enumeration.Type;
import com.generic.rest.api.project.search.entity.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class User extends AuditableBaseEntity {
    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;
}
