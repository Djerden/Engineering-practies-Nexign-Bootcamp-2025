package com.djeno.ipnexignbootcamp2025.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallType {

    OUTGOING(1), // Исходящий звонок
    INCOMING(2); // Входящий звонок

    private final int code;

    public static CallType fromCode(int code) {
        return switch (code) {
            case 1 -> OUTGOING;
            case 2 -> INCOMING;
            default -> throw new IllegalArgumentException("Неизвестный код CallType: " + code);
        };
    }
}
