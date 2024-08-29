package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.*;
import com.samatov.payment_service.enums.Currency;
import com.samatov.payment_service.enums.Language;
import com.samatov.payment_service.enums.PaymentMethod;
import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для PaymentServiceImpl")
class PaymentServiceImplTest {

    @Mock
    private AccountService accountService;
    @Mock
    private CardService cardService;
    @Mock
    private CustomerService customerService;
    @Mock
    private MerchantService merchantService;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен обработать запрос на пополнение")
    void shouldProcessTopUp() {
        TopUpRequestDto request = new TopUpRequestDto();
        request.setAmount(BigDecimal.valueOf(100));
        request.setCardData(new CardDataDto());
        request.setCustomer(new CustomerDto());

        CardDto cardDto = new CardDto();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setUserId(1L);
        AccountDto customerAccountDto = new AccountDto();
        customerAccountDto.setId(1L);
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setUserId(2L);
        AccountDto merchantAccountDto = new AccountDto();
        merchantAccountDto.setId(2L);
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(1L);
        transactionDto.setStatus(TransactionStatus.IN_PROGRESS);

        when(cardService.validateCard(any(), any(), any())).thenReturn(Mono.just(cardDto));
        when(customerService.findOrCreateCustomer(any())).thenReturn(Mono.just(customerDto));
        when(accountService.findByUserId(customerDto.getUserId())).thenReturn(Mono.just(customerAccountDto));
        when(merchantService.findDefaultMerchant()).thenReturn(Mono.just(merchantDto));
        when(accountService.findByUserId(merchantDto.getUserId())).thenReturn(Mono.just(merchantAccountDto));
        when(accountService.freezeFunds(any(), any())).thenReturn(Mono.just(customerAccountDto));
        when(transactionService.createTransaction(any())).thenReturn(Mono.just(transactionDto));

        StepVerifier.create(paymentService.processTopUp(request))
                .expectNextMatches(response ->
                        response.getTransactionId().equals(1L) &&
                                response.getStatus() == TransactionStatus.IN_PROGRESS &&
                                response.getMessage().equals("Transaction created successfully")
                )
                .verifyComplete();

        verify(cardService).validateCard(any(), any(), any());
        verify(customerService).findOrCreateCustomer(any());
        verify(accountService, times(2)).findByUserId(any());
        verify(merchantService).findDefaultMerchant();
        verify(accountService).freezeFunds(any(), any());
        verify(transactionService).createTransaction(any());
    }

    @Test
    @DisplayName("Должен обработать запрос на вывод средств")
    void shouldProcessWithdrawal() {
        PayoutRequestDto request = new PayoutRequestDto();
        request.setAmount(BigDecimal.valueOf(100));
        request.setMerchantId(1L);
        request.setCustomer(new CustomerDto());
        request.setCurrency(Currency.USD);
        request.setPaymentMethod(PaymentMethod.CARD);
        request.setLanguage(Language.EN);
        request.setNotificationUrl("http://example.com/webhook");

        CardDataDto cardDataDto = new CardDataDto();
        cardDataDto.setCardNumber("1234567890123456");
        cardDataDto.setExpDate("12/25");
        cardDataDto.setCvv("123");
        request.setCardData(cardDataDto);

        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setUserId(1L);
        AccountDto merchantAccountDto = new AccountDto();
        merchantAccountDto.setId(1L);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setUserId(2L);
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setCountry("US");
        AccountDto customerAccountDto = new AccountDto();
        customerAccountDto.setId(2L);
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(1L);
        transactionDto.setStatus(TransactionStatus.IN_PROGRESS);

        when(merchantService.findByUserId(1L)).thenReturn(Mono.just(merchantDto));
        when(accountService.findByUserId(merchantDto.getUserId())).thenReturn(Mono.just(merchantAccountDto));
        when(customerService.findOrCreateCustomer(any())).thenReturn(Mono.just(customerDto));
        when(accountService.findByUserId(customerDto.getUserId())).thenReturn(Mono.just(customerAccountDto));
        when(accountService.freezeFunds(any(), any())).thenReturn(Mono.just(merchantAccountDto));
        when(transactionService.createTransaction(any())).thenReturn(Mono.just(transactionDto));

        StepVerifier.create(paymentService.processWithdrawal(request))
                .expectNextMatches(response ->
                        response.getTransactionId().equals(1L) &&
                                response.getStatus() == TransactionStatus.IN_PROGRESS &&
                                response.getMessage().equals("Transaction created successfully")
                )
                .verifyComplete();

        verify(merchantService).findByUserId(1L);
        verify(accountService, times(2)).findByUserId(any());
        verify(customerService).findOrCreateCustomer(any());
        verify(accountService).freezeFunds(any(), any());
        verify(transactionService).createTransaction(any());
    }
}