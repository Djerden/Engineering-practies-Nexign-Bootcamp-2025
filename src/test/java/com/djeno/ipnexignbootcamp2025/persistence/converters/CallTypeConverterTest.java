package com.djeno.ipnexignbootcamp2025.persistence.converters;

import com.djeno.ipnexignbootcamp2025.persistence.enums.CallType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallTypeConverterTest {

    private final CallTypeConverter converter = new CallTypeConverter();

    @Test
    void convertToDatabaseColumn_ShouldConvertOutgoingToInteger() {
        assertEquals(1, converter.convertToDatabaseColumn(CallType.OUTGOING));
    }

    @Test
    void convertToDatabaseColumn_ShouldConvertIncomingToInteger() {
        assertEquals(2, converter.convertToDatabaseColumn(CallType.INCOMING));
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnNullForNullInput() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_ShouldConvertIntegerToOutgoing() {
        assertEquals(CallType.OUTGOING, converter.convertToEntityAttribute(1));
    }

    @Test
    void convertToEntityAttribute_ShouldConvertIntegerToIncoming() {
        assertEquals(CallType.INCOMING, converter.convertToEntityAttribute(2));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnNullForNullInput() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void convertToEntityAttribute_ShouldThrowExceptionForInvalidCode() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                converter.convertToEntityAttribute(3)
        );

        assertEquals("Неизвестный код CallType: 3", exception.getMessage());
    }
}
