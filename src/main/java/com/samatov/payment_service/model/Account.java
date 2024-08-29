package com.samatov.payment_service.model;

import com.samatov.payment_service.enums.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("accounts")
public class Account extends BaseEntity {
    private Long userId;
    private BigDecimal balance;
    private BigDecimal frozenAmount;
    private Currency currency;
}