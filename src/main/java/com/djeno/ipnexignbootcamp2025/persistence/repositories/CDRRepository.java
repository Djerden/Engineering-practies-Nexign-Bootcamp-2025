package com.djeno.ipnexignbootcamp2025.persistence.repositories;

import com.djeno.ipnexignbootcamp2025.persistence.models.CDR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CDRRepository extends JpaRepository<CDR, Long> {

    List<CDR> findByCaller_MsisdnOrReceiver_Msisdn(String callerMsisdn, String receiverMsisdn);

    @Query("SELECT c FROM CDR c WHERE (c.caller.msisdn = :msisdn OR c.receiver.msisdn = :msisdn) " +
            "AND c.startTime BETWEEN :startDate AND :endDate")
    List<CDR> findByMsisdnAndStartTimeBetween(
            @Param("msisdn") String msisdn,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
