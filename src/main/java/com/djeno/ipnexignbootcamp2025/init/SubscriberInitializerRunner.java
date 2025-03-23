package com.djeno.ipnexignbootcamp2025.init;

import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Order(1)
@Component
public class SubscriberInitializerRunner implements CommandLineRunner {

    private final SubscriberRepository subscriberRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> msisdns = Arrays.asList(
                "79811234567",
                "79118765432",
                "79815556677",
                "79994443322",
                "79993332211",
                "79992221133",
                "79111112233",
                "79990001122",
                "79998887766",
                "79997776655"
        );

        for (String msisdn : msisdns) {
            Subscriber subscriber = new Subscriber();
            subscriber.setMsisdn(msisdn);
            subscriberRepository.save(subscriber);
        }

        System.out.println("Абоненты успешно инициализированы");
    }
}
