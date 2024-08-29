package com.samatov.payment_service.model;

import com.samatov.payment_service.enums.CardType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("cards")
public class Card extends BaseEntity {
    private Long accountId;
    private String cardNumber;
    private CardType cardType;
    private String expDate;
    private String cvv;
}