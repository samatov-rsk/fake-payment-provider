package com.samatov.payment_service.model;

import com.samatov.payment_service.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("users")
public class User extends BaseEntity {
    private UserType userType;
}