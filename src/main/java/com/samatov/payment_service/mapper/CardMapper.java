package com.samatov.payment_service.mapper;

import com.samatov.payment_service.dto.CardDto;
import com.samatov.payment_service.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {
    CardDto toDto(Card card);
    Card toEntity(CardDto cardDto);
}