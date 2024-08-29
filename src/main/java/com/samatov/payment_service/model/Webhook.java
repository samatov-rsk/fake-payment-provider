package com.samatov.payment_service.model;

import com.samatov.payment_service.enums.WebhookStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Table("webhooks")
public class Webhook {
    @Id
    private Long id;
    private Long transactionId;
    private String url;
    private String requestBody;
    private String responseBody;
    private WebhookStatus status;
    private Integer attemptNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}