package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.PayoutRequestDto;
import com.samatov.payment_service.dto.PayoutResponseDto;
import com.samatov.payment_service.dto.TopUpRequestDto;
import com.samatov.payment_service.dto.TopUpResponseDto;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<TopUpResponseDto> processTopUp(TopUpRequestDto request);
    Mono<PayoutResponseDto> processWithdrawal(PayoutRequestDto request);
}