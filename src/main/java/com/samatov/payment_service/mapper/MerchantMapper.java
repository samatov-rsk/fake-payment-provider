package com.samatov.payment_service.mapper;

import com.samatov.payment_service.dto.MerchantDto;
import com.samatov.payment_service.model.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantMapper {
    MerchantDto toDto(Merchant merchant);
    Merchant toEntity(MerchantDto merchantDto);
}