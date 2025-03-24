package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.enums.CallType;
import com.djeno.ipnexignbootcamp2025.persistence.models.CDR;
import com.djeno.ipnexignbootcamp2025.persistence.models.Subscriber;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.CDRRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private CDRRepository cdrRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void generateReport_shouldCreateCorrectCDRFormat() {
        UUID requestId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2025, 2, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 11, 10, 0);
        List<CDR> mockCdrs = List.of(
                new CDR(1L, CallType.OUTGOING, new Subscriber("79996667755"), new Subscriber("79876543221"),
                        LocalDateTime.of(2025, 2, 10, 10, 12, 25), LocalDateTime.of(2025, 2, 10, 10, 12, 57))
        );

        when(cdrRepository.findByMsisdnAndStartTimeBetween("79996667755", start, end)).thenReturn(mockCdrs);

        String result = reportService.generateReport("79996667755", start, end, requestId, "csv");

        assertThat(result).contains("Отчет создан успешно");
        assertThat(Files.exists(Paths.get("./reports/" + "79996667755_" + requestId + ".csv"))).isTrue();
    }

    @Test
    void generateReport_shouldReturnMessageWhenNoCDRRecords() {
        UUID requestId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2025, 2, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 11, 10, 0);

        when(cdrRepository.findByMsisdnAndStartTimeBetween("79996667755", start, end)).thenReturn(Collections.emptyList());

        String result = reportService.generateReport("79996667755", start, end, requestId, "csv");

        assertThat(result).isEqualTo("Данные за указанный период не найдены");
    }

    @Test
    void generateReport_shouldHandleFileWriteError() throws IOException {
        UUID requestId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2025, 2, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 11, 10, 0);
        List<CDR> mockCdrs = List.of(
                new CDR(1L, CallType.OUTGOING, new Subscriber("79996667755"), new Subscriber("79876543221"),
                        LocalDateTime.of(2025, 2, 10, 10, 12, 25), LocalDateTime.of(2025, 2, 10, 10, 12, 57))
        );

        when(cdrRepository.findByMsisdnAndStartTimeBetween("79996667755", start, end)).thenReturn(mockCdrs);
        Files.createDirectories(Paths.get("./reports"));

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any())).thenThrow(new IOException("Ошибка"));

            String result = reportService.generateReport("79996667755", start, end, requestId, "csv");

            assertThat(result).contains("Ошибка при создании отчета");
        }
    }
}
