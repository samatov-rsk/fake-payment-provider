package com.samatov.payment_service.mapper;

import com.samatov.payment_service.dto.WebhookDto;
import com.samatov.payment_service.model.Webhook;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WebhookMapper {
    WebhookDto toDto(Webhook webhook);
    Webhook toEntity(WebhookDto webhookDto);
}