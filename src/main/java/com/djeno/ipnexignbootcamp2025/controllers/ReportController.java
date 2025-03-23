package com.djeno.ipnexignbootcamp2025.controllers;

import com.djeno.ipnexignbootcamp2025.persistence.DTO.GenerateReportResponse;
import com.djeno.ipnexignbootcamp2025.persistence.DTO.UdrReport;
import com.djeno.ipnexignbootcamp2025.services.ReportService;
import com.djeno.ipnexignbootcamp2025.services.UDRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

// 3 задача

@Tag(name = "Создание CDR отчета", description = "API для получения CDR отчетов (3 задача)")
@RequiredArgsConstructor
@RequestMapping("/cdr")
@RestController
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "Сгенерировать CDR-отчет в CSV",
            description = "Инициирует генерацию CDR-отчета для указанного абонента за указанный период в формате CSV.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
            }
    )
    @PostMapping("/generate-report-csv")
    public GenerateReportResponse generateCsvReport(
            @Parameter(description = "Номер абонента", example = "79991234567", required = true)
            @RequestParam String msisdn,

            @Parameter(description = "Начальная дата периода (ISO 8601)", example = "2025-01-01T00:00:00", required = true)
            @RequestParam String startDate,

            @Parameter(description = "Конечная дата периода (ISO 8601)", example = "2025-01-31T23:59:59", required = true)
            @RequestParam String endDate
    ) {
        UUID requestId = UUID.randomUUID();
        String status = reportService.generateReport(msisdn, startDate, endDate, requestId, "csv");
        return new GenerateReportResponse(requestId, status);
    }

    @Operation(
            summary = "Сгенерировать CDR-отчет в TXT",
            description = "Инициирует генерацию CDR-отчета для указанного абонента за указанный период в формате TXT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
            }
    )
    @PostMapping("/generate-report-txt")
    public GenerateReportResponse generateTxtReport(
            @Parameter(description = "Номер абонента", example = "79991234567", required = true)
            @RequestParam String msisdn,

            @Parameter(description = "Начальная дата периода (ISO 8601)", example = "2025-01-01T00:00:00", required = true)
            @RequestParam String startDate,

            @Parameter(description = "Конечная дата периода (ISO 8601)", example = "2025-01-31T23:59:59", required = true)
            @RequestParam String endDate
    ) {
        UUID requestId = UUID.randomUUID();
        String status = reportService.generateReport(msisdn, startDate, endDate, requestId, "txt");
        return new GenerateReportResponse(requestId, status);
    }
}
