package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.CDRRepository;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.SubscriberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CDRGeneratorServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private CDRRepository cdrRepository;

    @InjectMocks
    private CDRGeneratorService cdrGeneratorService;

    @Test
    void generateCDRsForYear_ShouldThrowException_WhenSubscribersLessThan10() {
        List<Subscriber> subscribers = List.of(new Subscriber("79991112233"));

        when(subscriberRepository.findAll()).thenReturn(subscribers);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                cdrGeneratorService.generateCDRsForYear(2025));

        assertEquals("Необходимо минимум 10 абонентов в базе.", exception.getMessage());
    }

    @Test
    void generateCDRsForYear_ShouldGenerateCDRs_WhenEnoughSubscribers() {
        List<Subscriber> subscribers = IntStream.range(0, 10)
                .mapToObj(i -> new Subscriber("7999111223" + i))
                .collect(Collectors.toList());

        when(subscriberRepository.findAll()).thenReturn(subscribers);

        cdrGeneratorService.generateCDRsForYear(2025);

        verify(cdrRepository, atLeastOnce()).saveAll(any());
    }

    @Test
    void getRandomReceiver_ShouldReturnDifferentSubscriber() {
        List<Subscriber> subscribers = List.of(
                new Subscriber("79991112233"),
                new Subscriber("79991112244"),
                new Subscriber("79991112255")
        );

        Subscriber receiver = cdrGeneratorService.getRandomReceiver(subscribers, "79991112233");

        assertNotNull(receiver);
        assertNotEquals("79991112233", receiver.getMsisdn());
    }
}
