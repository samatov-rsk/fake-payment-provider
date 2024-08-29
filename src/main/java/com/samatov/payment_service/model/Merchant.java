package com.samatov.payment_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("merchants")
public class Merchant extends BaseEntity {
    private Long userId;
}