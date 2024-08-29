package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.*;
import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.enums.TransactionType;
import com.samatov.payment_service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountService accountService;
    private final CardService cardService;
    private final CustomerService customerService;
    private final MerchantService merchantService;
    private final TransactionService transactionService;

    @Override
    public Mono<TopUpResponseDto> processTopUp(TopUpRequestDto request) {
        log.info("Processing top-up request: {}", request);
        return cardService.validateCard(request.getCardData().getCardNumber(), request.getCardData().getExpDate(), request.getCardData().getCvv())
                .flatMap(card -> customerService.findOrCreateCustomer(request.getCustomer()))
                .flatMap(customer -> accountService.findByUserId(customer.getUserId())
                        .flatMap(customerAccount ->
                                merchantService.findDefaultMerchant()
                                        .flatMap(merchant -> accountService.findByUserId(merchant.getUserId())
                                                .flatMap(merchantAccount ->
                                                        accountService.freezeFunds(customerAccount.getId(), request.getAmount())
                                                                .then(createTopUpTransaction(customerAccount.getId(), merchantAccount.getId(), request, customer))
                                                )
                                        )
                        )
                )
                .map(this::createTopUpResponse)
                .doOnSuccess(response -> log.info("Top-up request processed successfully: {}", response))
                .doOnError(error -> log.error("Error processing top-up request", error));
    }

    @Override
    public Mono<PayoutResponseDto> processWithdrawal(PayoutRequestDto request) {
        log.info("Processing withdrawal request: {}", request);
        return merchantService.findByUserId(request.getMerchantId())
                .flatMap(merchant -> accountService.findByUserId(merchant.getUserId())
                        .flatMap(merchantAccount ->
                                customerService.findOrCreateCustomer(request.getCustomer())
                                        .flatMap(customer -> accountService.findByUserId(customer.getUserId())
                                                .flatMap(customerAccount ->
                                                        accountService.freezeFunds(merchantAccount.getId(), request.getAmount())
                                                                .then(createWithdrawalTransaction(merchantAccount.getId(), customerAccount.getId(), request, customer))
                                                )
                                        )
                        )
                )
                .map(this::createPayoutResponse)
                .doOnSuccess(response -> log.info("Withdrawal request processed successfully: {}", response))
                .doOnError(error -> log.error("Error processing withdrawal request", error));
    }

    private Mono<TransactionDto> createTopUpTransaction(Long accountFromId, Long accountToId, TopUpRequestDto request, CustomerDto customer) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountFrom(accountFromId);
        transactionDto.setAccountTo(accountToId);
        transactionDto.setAmount(request.getAmount());
        transactionDto.setCurrency(request.getCurrency());
        transactionDto.setPaymentMethod(request.getPaymentMethod());
        transactionDto.setCardNumber(request.getCardData().getCardNumber());
        transactionDto.setLanguage(request.getLanguage());
        transactionDto.setNotificationUrl(request.getNotificationUrl());
        transactionDto.setStatus(TransactionStatus.IN_PROGRESS);
        transactionDto.setType(TransactionType.TOP_UP);
        transactionDto.setCustomerFirstName(customer.getFirstName());
        transactionDto.setCustomerLastName(customer.getLastName());
        transactionDto.setCustomerCountry(customer.getCountry());
        transactionDto.setMessage("Transaction created successfully");
        return transactionService.createTransaction(transactionDto);
    }

    private Mono<TransactionDto> createWithdrawalTransaction(Long accountFromId, Long accountToId, PayoutRequestDto request, CustomerDto customer) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountFrom(accountFromId);
        transactionDto.setAccountTo(accountToId);
        transactionDto.setAmount(request.getAmount());
        transactionDto.setCurrency(request.getCurrency());
        transactionDto.setPaymentMethod(request.getPaymentMethod());
        transactionDto.setCardNumber(request.getCardData().getCardNumber());
        transactionDto.setLanguage(request.getLanguage());
        transactionDto.setNotificationUrl(request.getNotificationUrl());
        transactionDto.setStatus(TransactionStatus.IN_PROGRESS);
        transactionDto.setType(TransactionType.WITHDRAWAL);
        transactionDto.setCustomerFirstName(customer.getFirstName());
        transactionDto.setCustomerLastName(customer.getLastName());
        transactionDto.setCustomerCountry(customer.getCountry());
        transactionDto.setMessage("Transaction created successfully");
        return transactionService.createTransaction(transactionDto);
    }

    private TopUpResponseDto createTopUpResponse(TransactionDto transaction) {
        TopUpResponseDto response = new TopUpResponseDto();
        response.setTransactionId(transaction.getId());
        response.setStatus(transaction.getStatus());
        response.setMessage("Transaction created successfully");
        return response;
    }

    private PayoutResponseDto createPayoutResponse(TransactionDto transaction) {
        PayoutResponseDto response = new PayoutResponseDto();
        response.setTransactionId(transaction.getId());
        response.setStatus(transaction.getStatus());
        response.setMessage("Transaction created successfully");
        return response;
    }
}