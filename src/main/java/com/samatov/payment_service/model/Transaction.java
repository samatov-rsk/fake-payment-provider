package com.samatov.payment_service.model;

import com.samatov.payment_service.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@Table("transactions")
public class Transaction extends BaseEntity {
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;
    private Currency currency;
    private PaymentMethod paymentMethod;
    private String cardNumber;
    private Language language;
    private String notificationUrl;
    private TransactionStatus status;
    private String message;
    private TransactionType type;
    private String customerFirstName;
    private String customerLastName;
    private String customerCountry;
}