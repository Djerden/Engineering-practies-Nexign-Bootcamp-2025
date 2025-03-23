package com.djeno.ipnexignbootcamp2025.persistence.DTO;

import lombok.Data;

@Data
public class UdrReport {
    private String msisdn;
    private CallDetail incomingCall;
    private CallDetail outcomingCall;
}
