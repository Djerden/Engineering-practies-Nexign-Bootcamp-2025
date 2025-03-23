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

import java.util.List;
import java.util.Map;
import java.util.UUID;

// 2 задача

@Tag(name = "Получение UDR отчетов", description = "API для получения UDR отчетов (2 задача)")
@RequiredArgsConstructor
@RequestMapping("/udr")
@RestController
public class UDRController {

    private final UDRService udrService;

    @Operation(
            summary = "Получить UDR-отчет по абоненту",
            description = "Возвращает UDR-отчет для указанного абонента за указанный месяц и год или за весь период.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос"),
                    @ApiResponse(responseCode = "404", description = "Данные не найдены")
            }
    )
    @GetMapping("/report")
    public UdrReport getUdrReport(
            @Parameter(description = "Номер абонента", example = "79991234567", required = true)
            @RequestParam String msisdn,

            @Parameter(description = "Год (например, 2024). Если не указан, возвращаются данные за весь период.", example = "2024")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "Месяц (1-12). Если не указан, возвращаются данные за весь период.", example = "2")
            @RequestParam(required = false) Integer month
    ) {
        return udrService.getUdrReport(msisdn, year, month);
    }

    @Operation(
            summary = "Получить UDR-отчеты по всем абонентам за месяц и год",
            description = "Возвращает UDR-отчеты для всех абонентов за указанный месяц и год.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос"),
                    @ApiResponse(responseCode = "404", description = "Данные не найдены")
            }
    )
    @GetMapping("/reports")
    public List<UdrReport> getUdrReportsForMonth(
            @Parameter(description = "Год (например, 2024)", example = "2024", required = true)
            @RequestParam int year,

            @Parameter(description = "Месяц (1-12)", example = "2", required = true)
            @RequestParam int month
    ) {
        return udrService.getUdrReportsForMonth(year, month);
    }
}
