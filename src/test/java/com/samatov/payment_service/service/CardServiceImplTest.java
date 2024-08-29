package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.CardDto;
import com.samatov.payment_service.mapper.CardMapper;
import com.samatov.payment_service.model.Card;
import com.samatov.payment_service.repository.CardRepository;
import com.samatov.payment_service.service.impl.CardServiceImpl;
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

@DisplayName("Тесты для CardServiceImpl")
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен найти карту по ID")
    void shouldFindCardById() {
        Long cardId = 1L;
        Card card = new Card();
        CardDto cardDto = new CardDto();

        when(cardRepository.findById(cardId)).thenReturn(Mono.just(card));
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        StepVerifier.create(cardService.findById(cardId))
                .expectNext(cardDto)
                .verifyComplete();

        verify(cardRepository).findById(cardId);
        verify(cardMapper).toDto(card);
    }

    @Test
    @DisplayName("Должен найти все карты")
    void shouldFindAllCards() {
        Card card1 = new Card();
        Card card2 = new Card();
        CardDto cardDto1 = new CardDto();
        CardDto cardDto2 = new CardDto();

        when(cardRepository.findAll()).thenReturn(Flux.just(card1, card2));
        when(cardMapper.toDto(card1)).thenReturn(cardDto1);
        when(cardMapper.toDto(card2)).thenReturn(cardDto2);

        StepVerifier.create(cardService.findAll())
                .expectNext(cardDto1, cardDto2)
                .verifyComplete();

        verify(cardRepository).findAll();
        verify(cardMapper, times(2)).toDto(any(Card.class));
    }

    @Test
    @DisplayName("Должен создать новую карту")
    void shouldCreateCard() {
        CardDto inputDto = new CardDto();
        Card card = new Card();
        CardDto outputDto = new CardDto();

        when(cardMapper.toEntity(inputDto)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(Mono.just(card));
        when(cardMapper.toDto(card)).thenReturn(outputDto);

        StepVerifier.create(cardService.createCard(inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(cardMapper).toEntity(inputDto);
        verify(cardRepository).save(card);
        verify(cardMapper).toDto(card);
    }

    @Test
    @DisplayName("Должен обновить существующую карту")
    void shouldUpdateCard() {
        Long cardId = 1L;
        CardDto inputDto = new CardDto();
        Card existingCard = new Card();
        Card updatedCard = new Card();
        CardDto outputDto = new CardDto();

        when(cardRepository.findById(cardId)).thenReturn(Mono.just(existingCard));
        when(cardRepository.save(existingCard)).thenReturn(Mono.just(updatedCard));
        when(cardMapper.toDto(updatedCard)).thenReturn(outputDto);

        StepVerifier.create(cardService.updateCard(cardId, inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(cardRepository).findById(cardId);
        verify(cardRepository).save(existingCard);
        verify(cardMapper).toDto(updatedCard);
    }

    @Test
    @DisplayName("Должен удалить карту")
    void shouldDeleteCard() {
        Long cardId = 1L;

        when(cardRepository.deleteById(cardId)).thenReturn(Mono.empty());

        StepVerifier.create(cardService.deleteCard(cardId))
                .verifyComplete();

        verify(cardRepository).deleteById(cardId);
    }

    @Test
    @DisplayName("Должен валидировать карту")
    void shouldValidateCard() {
        String cardNumber = "1234567890123456";
        String expDate = "12/25";
        String cvv = "123";
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setExpDate(expDate);
        card.setCvv(cvv);
        CardDto cardDto = new CardDto();
        cardDto.setCardNumber(cardNumber);
        cardDto.setExpDate(expDate);
        cardDto.setCvv(cvv);

        when(cardRepository.findByCardNumber(cardNumber)).thenReturn(Mono.just(card));
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        StepVerifier.create(cardService.validateCard(cardNumber, expDate, cvv))
                .expectNext(cardDto)
                .verifyComplete();

        verify(cardRepository).findByCardNumber(cardNumber);
        verify(cardMapper).toDto(card);
    }
}
