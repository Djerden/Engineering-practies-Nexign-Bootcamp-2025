package com.djeno.ipnexignbootcamp2025.services;

import com.djeno.ipnexignbootcamp2025.persistence.models.CDR;
import com.djeno.ipnexignbootcamp2025.persistence.repositories.CDRRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final CDRRepository cdrRepository;

    public String generateReport(String msisdn, LocalDateTime startDate, LocalDateTime endDate, UUID requestId, String format) {
        List<CDR> cdrs = cdrRepository.findByMsisdnAndStartTimeBetween(msisdn, startDate, endDate);

        if (cdrs.isEmpty()) {
            return "Данные за указанный период не найдены";
        }

        String fileExtension = format.equalsIgnoreCase("txt") ? ".txt" : ".csv";
        String fileName = msisdn + "_" + requestId + fileExtension;
        String directoryPath = "./reports";
        String filePath = directoryPath + "/" + fileName;

        try {
            Files.createDirectories(Paths.get(directoryPath));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (CDR cdr : cdrs) {
                    writer.write(formatCdr(cdr) + "\n");
                }
            }

            return "Отчет создан успешно. Файл: " + filePath;
        } catch (IOException e) {
            return "Ошибка при создании отчета: " + e.getMessage();
        }
    }

    private String formatCdr(CDR cdr) {
        return String.format("%02d,%s,%s,%s,%s",
                cdr.getCallType().getCode(),
                cdr.getCaller().getMsisdn(),
                cdr.getReceiver().getMsisdn(),
                cdr.getStartTime(),
                cdr.getEndTime()
        );
    }
}