package com.djeno.ipnexignbootcamp2025.persistence.repositories;

import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {

}
