package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.MerchantDto;
import com.samatov.payment_service.exception.MerchantNotFoundException;
import com.samatov.payment_service.mapper.MerchantMapper;
import com.samatov.payment_service.model.Merchant;
import com.samatov.payment_service.repository.MerchantRepository;
import com.samatov.payment_service.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для MerchantServiceImpl")
class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен найти мерчанта по ID")
    void shouldFindMerchantById() {
        Long merchantId = 1L;
        Merchant merchant = new Merchant();
        MerchantDto merchantDto = new MerchantDto();

        when(merchantRepository.findById(merchantId)).thenReturn(Mono.just(merchant));
        when(merchantMapper.toDto(merchant)).thenReturn(merchantDto);

        StepVerifier.create(merchantService.findById(merchantId))
                .expectNext(merchantDto)
                .verifyComplete();

        verify(merchantRepository).findById(merchantId);
        verify(merchantMapper).toDto(merchant);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если мерчант не найден")
    void shouldThrowExceptionWhenMerchantNotFound() {
        Long merchantId = 1L;

        when(merchantRepository.findById(merchantId)).thenReturn(Mono.empty());

        StepVerifier.create(merchantService.findById(merchantId))
                .expectError(MerchantNotFoundException.class)
                .verify();

        verify(merchantRepository).findById(merchantId);
    }

    @Test
    @DisplayName("Должен найти мерчанта по ID пользователя")
    void shouldFindMerchantByUserId() {
        Long userId = 1L;
        Merchant merchant = new Merchant();
        MerchantDto merchantDto = new MerchantDto();

        when(merchantRepository.findByUserId(userId)).thenReturn(Mono.just(merchant));
        when(merchantMapper.toDto(merchant)).thenReturn(merchantDto);

        StepVerifier.create(merchantService.findByUserId(userId))
                .expectNext(merchantDto)
                .verifyComplete();

        verify(merchantRepository).findByUserId(userId);
        verify(merchantMapper).toDto(merchant);
    }

    @Test
    @DisplayName("Должен найти всех мерчантов")
    void shouldFindAllMerchants() {
        Merchant merchant1 = new Merchant();
        Merchant merchant2 = new Merchant();
        MerchantDto merchantDto1 = new MerchantDto();
        MerchantDto merchantDto2 = new MerchantDto();

        when(merchantRepository.findAll()).thenReturn(Flux.just(merchant1, merchant2));
        when(merchantMapper.toDto(merchant1)).thenReturn(merchantDto1);
        when(merchantMapper.toDto(merchant2)).thenReturn(merchantDto2);

        StepVerifier.create(merchantService.findAll())
                .expectNext(merchantDto1, merchantDto2)
                .verifyComplete();

        verify(merchantRepository).findAll();
        verify(merchantMapper, times(2)).toDto(any(Merchant.class));
    }

    @Test
    @DisplayName("Должен создать нового мерчанта")
    void shouldCreateMerchant() {
        MerchantDto inputDto = new MerchantDto();
        Merchant merchant = new Merchant();
        MerchantDto outputDto = new MerchantDto();

        when(merchantMapper.toEntity(inputDto)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(Mono.just(merchant));
        when(merchantMapper.toDto(merchant)).thenReturn(outputDto);

        StepVerifier.create(merchantService.createMerchant(inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(merchantMapper).toEntity(inputDto);
        verify(merchantRepository).save(merchant);
        verify(merchantMapper).toDto(merchant);
    }

    @Test
    @DisplayName("Должен обновить существующего мерчанта")
    void shouldUpdateMerchant() {
        Long merchantId = 1L;
        MerchantDto inputDto = new MerchantDto();
        Merchant existingMerchant = new Merchant();
        Merchant updatedMerchant = new Merchant();
        MerchantDto outputDto = new MerchantDto();

        when(merchantRepository.findById(merchantId)).thenReturn(Mono.just(existingMerchant));
        when(merchantRepository.save(existingMerchant)).thenReturn(Mono.just(updatedMerchant));
        when(merchantMapper.toDto(updatedMerchant)).thenReturn(outputDto);

        StepVerifier.create(merchantService.updateMerchant(merchantId, inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(merchantRepository).findById(merchantId);
        verify(merchantRepository).save(existingMerchant);
        verify(merchantMapper).toDto(updatedMerchant);
    }

    @Test
    @DisplayName("Должен удалить мерчанта")
    void shouldDeleteMerchant() {
        Long merchantId = 1L;

        when(merchantRepository.deleteById(merchantId)).thenReturn(Mono.empty());

        StepVerifier.create(merchantService.deleteMerchant(merchantId))
                .verifyComplete();

        verify(merchantRepository).deleteById(merchantId);
    }
}
