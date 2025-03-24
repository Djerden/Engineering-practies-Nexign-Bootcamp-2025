package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.enums.CallType;
import com.djeno.ipnexignbootcamp2025.persistence.models.CDR;
import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.CDRRepository;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CDRGeneratorService {

    private final SubscriberRepository subscriberRepository;
    private final CDRRepository cdrRepository;
    private final Random random = new Random();

    public void generateCDRsForYear(int year) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        if (subscribers.size() < 10) {
            throw new IllegalStateException("Необходимо минимум 10 абонентов в базе.");
        }

        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59);

        LocalDateTime currentDate = startDate;

        while (currentDate.isBefore(endDate)) {
            int callsPerDay = random.nextInt(5) + 1; // 1-5 звонков в день
            Collections.shuffle(subscribers);

            List<CDR> dailyCDRs = new ArrayList<>();

            for (int i = 0; i < callsPerDay; i++) {
                Subscriber caller = subscribers.get(random.nextInt(subscribers.size()));
                Subscriber receiver = getRandomReceiver(subscribers, caller.getMsisdn());

                if (receiver == null) continue;

                CDR cdr = generateCDR(caller, receiver, currentDate);
                dailyCDRs.add(cdr);

                // Случайное время в течение дня (до 1 часа между звонками)
                currentDate = currentDate.plusSeconds(random.nextInt(3600));
            }
            cdrRepository.saveAll(dailyCDRs);

            currentDate = currentDate.withHour(0).withMinute(0).withSecond(0).plusDays(1);
        }
    }

    public CDR generateCDR(Subscriber caller, Subscriber receiver, LocalDateTime startTime) {
        return new CDR(
                null,
                random.nextBoolean() ? CallType.OUTGOING : CallType.INCOMING,
                caller,
                receiver,
                startTime,
                startTime.plusSeconds(random.nextInt(300) + 60) // Длительность 1-5 минут
        );
    }

    public Subscriber getRandomReceiver(List<Subscriber> subscribers, String callerNumber) {
        List<Subscriber> filteredSubscribers = subscribers.stream()
                .filter(sub -> !sub.getMsisdn().equals(callerNumber))
                .collect(Collectors.toList());

        if (filteredSubscribers.isEmpty()) {
            return null;
        }

        return filteredSubscribers.get(random.nextInt(filteredSubscribers.size()));
    }
}

