package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.SubscriberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private SubscriberService subscriberService;

    @Test
    void saveSubscribers_ShouldSaveOnlyNewSubscribers() {
        List<String> msisdns = List.of("79991112233", "79992223344", "79993334455");

        when(subscriberRepository.existsById("79991112233")).thenReturn(true);  // Уже существует
        when(subscriberRepository.existsById("79992223344")).thenReturn(false); // Новый
        when(subscriberRepository.existsById("79993334455")).thenReturn(false); // Новый

        subscriberService.saveSubscribers(msisdns);

        ArgumentCaptor<List<Subscriber>> captor = ArgumentCaptor.forClass(List.class);
        verify(subscriberRepository).saveAll(captor.capture());

        List<Subscriber> savedSubscribers = captor.getValue();
        assertEquals(2, savedSubscribers.size()); // Должны сохраниться только 2 новых
        assertEquals("79992223344", savedSubscribers.get(0).getMsisdn());
        assertEquals("79993334455", savedSubscribers.get(1).getMsisdn());
    }

    @Test
    void saveSubscribers_ShouldNotSaveIfAllExist() {
        List<String> msisdns = List.of("79991112233", "79992223344");

        when(subscriberRepository.existsById(anyString())).thenReturn(true); // Все уже есть

        subscriberService.saveSubscribers(msisdns);

        verify(subscriberRepository, never()).saveAll(any()); // Метод saveAll не должен вызываться
    }
}

