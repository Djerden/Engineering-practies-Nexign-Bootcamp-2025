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

    public String generateReport(String msisdn, String startDate, String endDate, UUID requestId, String format) {
        LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);

        List<CDR> cdrs = cdrRepository.findByMsisdnAndStartTimeBetween(msisdn, start, end);

        if (cdrs.isEmpty()) {
            return "Данные за указанный период не найдены";
        }

        String fileExtension = format.equalsIgnoreCase("txt") ? ".txt" : ".csv";
        String fileName = msisdn + "_" + requestId + fileExtension;
        String directoryPath = "./reports";
        String filePath = directoryPath + "/" + fileName;

        try {
            // Создаем директорию, если она не существует
            Files.createDirectories(Paths.get(directoryPath));

            // Записываем данные в файл
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                if (format.equalsIgnoreCase("csv")) {
                    writer.write("ID,Caller,Receiver,CallType,StartTime,EndTime,Duration\n");
                }
                for (CDR cdr : cdrs) {
                    writer.write(formatCdr(cdr, format) + "\n");
                }
            }

            return "Отчет создан успешно. Файл: " + filePath;
        } catch (IOException e) {
            return "Ошибка при создании отчета: " + e.getMessage();
        }
    }

    private String formatCdr(CDR cdr, String format) {
        Duration duration = Duration.between(cdr.getStartTime(), cdr.getEndTime());

        if (format.equalsIgnoreCase("csv")) {
            return String.format("%d,%s,%s,%s,%s,%s,%02d:%02d:%02d",
                    cdr.getId(),
                    cdr.getCaller().getMsisdn(),
                    cdr.getReceiver().getMsisdn(),
                    cdr.getCallType().name(),
                    cdr.getStartTime(),
                    cdr.getEndTime(),
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart()
            );
        } else { // TXT формат
            return String.format("ID: %d | Caller: %s | Receiver: %s | CallType: %s | StartTime: %s | EndTime: %s | Duration: %02d:%02d:%02d",
                    cdr.getId(),
                    cdr.getCaller().getMsisdn(),
                    cdr.getReceiver().getMsisdn(),
                    cdr.getCallType().name(),
                    cdr.getStartTime(),
                    cdr.getEndTime(),
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart()
            );
        }
    }
}