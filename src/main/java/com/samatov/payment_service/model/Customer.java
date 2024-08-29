package com.samatov.payment_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("customers")
public class Customer extends BaseEntity {
    private Long userId;
    private String firstName;
    private String lastName;
    private String country;
}