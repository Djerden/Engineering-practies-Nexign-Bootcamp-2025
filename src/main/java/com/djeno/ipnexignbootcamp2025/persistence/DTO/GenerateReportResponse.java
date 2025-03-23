package com.djeno.ipnexignbootcamp2025.persistence.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenerateReportResponse {
    private UUID requestId;
    private String status;
}
