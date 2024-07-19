package com.generic.rest.api.project.model;

import com.generic.rest.api.project.search.entity.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Where(clause="deleted=0")
@Table(name = "permission_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends AuditableBaseEntity {
    private String abbr;
    private String name;
}
