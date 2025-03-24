package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.DTO.UdrReport;
import com.djeno.ipnexignbootcamp2025.persistence.enums.CallType;
import com.djeno.ipnexignbootcamp2025.persistence.models.CDR;
import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.CDRRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UDRServiceTest {

    @Mock
    private CDRRepository cdrRepository;

    @InjectMocks
    private UDRService udrService;

    private static final String TEST_MSISDN = "79991112233";

    @Test
    void getUdrReport_ShouldCalculateDurationsCorrectly() {
        // Arrange
        List<CDR> cdrs = List.of(
                createCDR(TEST_MSISDN, "79992223344", CallType.OUTGOING, 10),
                createCDR("79992223344", TEST_MSISDN, CallType.INCOMING, 20),
                createCDR(TEST_MSISDN, "79993334455", CallType.OUTGOING, 15)
        );

        when(cdrRepository.findByCaller_MsisdnOrReceiver_Msisdn(TEST_MSISDN, TEST_MSISDN)).thenReturn(cdrs);

        // Act
        UdrReport report = udrService.getUdrReport(TEST_MSISDN, null, null);

        // Assert
        assertEquals(TEST_MSISDN, report.getMsisdn());
        assertEquals("00:00:20", report.getIncomingCall().getTotalTime());
        assertEquals("00:00:25", report.getOutcomingCall().getTotalTime()); // 10 + 15
    }

    @Test
    void getUdrReport_ShouldFilterByYearAndMonth() {
        // Arrange
        int year = 2024;
        int month = 3;

        List<CDR> cdrs = List.of(
                createCDR(TEST_MSISDN, "79992223344", CallType.OUTGOING,
                        LocalDateTime.of(2024, 3, 10, 12, 0),
                        LocalDateTime.of(2024, 3, 10, 12, 10)), // 10 минут

                createCDR(TEST_MSISDN, "79993334455", CallType.OUTGOING,
                        LocalDateTime.of(2023, 3, 10, 12, 0),
                        LocalDateTime.of(2023, 3, 10, 12, 15)) // Должен быть проигнорирован
        );

        when(cdrRepository.findByCaller_MsisdnOrReceiver_Msisdn(TEST_MSISDN, TEST_MSISDN)).thenReturn(cdrs);

        // Act
        UdrReport report = udrService.getUdrReport(TEST_MSISDN, year, month);

        // Assert
        assertEquals(TEST_MSISDN, report.getMsisdn());
        assertEquals("00:10:00", report.getOutcomingCall().getTotalTime());
    }

    @Test
    void getUdrReportsForMonth_ShouldAggregateDataCorrectly() {
        int year = 2024;
        int month = 3;

        List<CDR> cdrs = List.of(
                createCDR("79991112233", "79992223344", CallType.OUTGOING,
                        LocalDateTime.of(2024, 3, 5, 14, 0),
                        LocalDateTime.of(2024, 3, 5, 14, 10)),  // 10 минут

                createCDR("79991112233", "79993334455", CallType.OUTGOING,
                        LocalDateTime.of(2024, 3, 7, 16, 0),
                        LocalDateTime.of(2024, 3, 7, 16, 30)),  // 30 минут

                createCDR("79992223344", "79991112233", CallType.OUTGOING,
                        LocalDateTime.of(2024, 3, 6, 15, 0),
                        LocalDateTime.of(2024, 3, 6, 15, 20))   // 20 минут
        );

        when(cdrRepository.findAll()).thenReturn(cdrs);

        List<UdrReport> reports = udrService.getUdrReportsForMonth(year, month);

        assertEquals(3, reports.size());

        UdrReport report1 = reports.stream().filter(r -> r.getMsisdn().equals("79991112233")).findFirst().orElseThrow();
        assertEquals("00:40:00", report1.getOutcomingCall().getTotalTime());
        assertEquals("00:20:00", report1.getIncomingCall().getTotalTime());

        UdrReport report2 = reports.stream().filter(r -> r.getMsisdn().equals("79992223344")).findFirst().orElseThrow();
        assertEquals("00:20:00", report2.getOutcomingCall().getTotalTime());
        assertEquals("00:10:00", report2.getIncomingCall().getTotalTime());

        UdrReport report3 = reports.stream().filter(r -> r.getMsisdn().equals("79993334455")).findFirst().orElseThrow();
        assertEquals("00:00:00", report3.getOutcomingCall().getTotalTime());
        assertEquals("00:30:00", report3.getIncomingCall().getTotalTime());
    }

    private CDR createCDR(String caller, String receiver, CallType callType, int durationSeconds) {
        LocalDateTime startTime = LocalDateTime.now();
        return createCDR(caller, receiver, callType, startTime, startTime.plusSeconds(durationSeconds));
    }

    private CDR createCDR(String caller, String receiver, CallType callType, LocalDateTime startTime, LocalDateTime endTime) {
        CDR cdr = new CDR();
        cdr.setCaller(new Subscriber(caller));
        cdr.setReceiver(new Subscriber(receiver));
        cdr.setCallType(callType);
        cdr.setStartTime(startTime);
        cdr.setEndTime(endTime);
        return cdr;
    }
}
