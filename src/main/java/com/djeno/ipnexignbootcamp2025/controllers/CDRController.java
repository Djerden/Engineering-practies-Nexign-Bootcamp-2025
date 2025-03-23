package com.djeno.ipnexignbootcamp2025.controllers;

import com.djeno.ipnexignbootcamp2025.persistence.DTO.GenerateReportResponse;
import com.djeno.ipnexignbootcamp2025.persistence.DTO.UdrReport;
import com.djeno.ipnexignbootcamp2025.services.CDRGeneratorService;
import com.djeno.ipnexignbootcamp2025.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 1 задача

@Tag(name = "Генератор CDR записей", description = "API генерации CDR записей для существующих абонентов (1 задача)")
@RequiredArgsConstructor
@RequestMapping("/cdr")
@RestController
public class CDRController {

    private final CDRGeneratorService cdrGeneratorService;
    private final ReportService reportService;

    @Operation(
            summary = "Сгенерировать CDR-записи за год",
            description = "Создает фиктивные CDR-записи для указанного года.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CDR-записи успешно сгенерированы"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
            }
    )
    @PostMapping("/generate")
    public ResponseEntity<String> generateCDRs(
            @Parameter(description = "Год, за который необходимо сгенерировать CDR-записи", example = "2024", required = true)
            @RequestParam int year) {
        cdrGeneratorService.generateCDRsForYear(year);
        return ResponseEntity.ok("CDR записи за " + year + " успешно сгенерированы!");
    }
}
