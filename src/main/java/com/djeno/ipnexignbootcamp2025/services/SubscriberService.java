package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    /**
     * Сохраняет список абонентов, игнорируя уже существующих.
     *
     * @param msisdns список номеров абонентов
     */
    public void saveSubscribers(List<String> msisdns) {
        List<Subscriber> newSubscribers = msisdns.stream()
                .filter(msisdn -> !subscriberRepository.existsById(msisdn))
                .map(Subscriber::new)
                .collect(Collectors.toList());

        subscriberRepository.saveAll(newSubscribers);
    }
}
