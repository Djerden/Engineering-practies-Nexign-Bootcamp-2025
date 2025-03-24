package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.enums.CallType;
import com.djeno.ipnexignbootcamp2025.persistence.models.CDR;
import com.djeno.ipnexignbootcamp2025.persistence.DTO.CallDetail;
import com.djeno.ipnexignbootcamp2025.persistence.DTO.UdrReport;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.CDRRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UDRService {

    private final CDRRepository cdrRepository;

    public UdrReport getUdrReport(String msisdn, Integer year, Integer month) {
        List<CDR> cdrs = cdrRepository.findByCaller_MsisdnOrReceiver_Msisdn(msisdn, msisdn);
        Duration incomingDuration = Duration.ZERO;
        Duration outgoingDuration = Duration.ZERO;

        for (CDR cdr : cdrs) {
            if (shouldSkipRecord(cdr, year, month)) {
                continue;
            }

            Duration duration = Duration.between(cdr.getStartTime(), cdr.getEndTime());

            outgoingDuration = outgoingDuration.plus(
                    cdr.getCaller().getMsisdn().equals(msisdn) ? duration : Duration.ZERO
            );

            incomingDuration = incomingDuration.plus(
                    cdr.getReceiver().getMsisdn().equals(msisdn) ? duration : Duration.ZERO
            );
        }

        UdrReport report = new UdrReport();
        report.setMsisdn(msisdn);
        report.setIncomingCall(new CallDetail(formatDuration(incomingDuration)));
        report.setOutcomingCall(new CallDetail(formatDuration(outgoingDuration)));

        return report;
    }

    public List<UdrReport> getUdrReportsForMonth(int year, int month) {
        List<CDR> cdrs = cdrRepository.findAll();
        Map<String, Duration> incomingDurations = new HashMap<>();
        Map<String, Duration> outgoingDurations = new HashMap<>();

        for (CDR cdr : cdrs) {
            if (shouldSkipRecord(cdr, year, month)) {
                continue;
            }

            Duration duration = Duration.between(cdr.getStartTime(), cdr.getEndTime());

            outgoingDurations.merge(cdr.getCaller().getMsisdn(), duration, Duration::plus);
            incomingDurations.merge(cdr.getReceiver().getMsisdn(), duration, Duration::plus);
        }

        Set<String> allMsisdns = new HashSet<>();
        allMsisdns.addAll(incomingDurations.keySet());
        allMsisdns.addAll(outgoingDurations.keySet());

        List<UdrReport> reports = new ArrayList<>();
        for (String msisdn : allMsisdns) {
            UdrReport report = new UdrReport();
            report.setMsisdn(msisdn);
            report.setIncomingCall(new CallDetail(formatDuration(incomingDurations.getOrDefault(msisdn, Duration.ZERO))));
            report.setOutcomingCall(new CallDetail(formatDuration(outgoingDurations.getOrDefault(msisdn, Duration.ZERO))));

            reports.add(report);
        }

        return reports;
    }

    private boolean shouldSkipRecord(CDR cdr, Integer year, Integer month) {
        boolean wrongYear = (year != null && cdr.getStartTime().getYear() != year);
        boolean wrongMonth = (month != null && cdr.getStartTime().getMonthValue() != month);

        if (wrongYear || wrongMonth) {
            return true;
        }
        return false;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
