package com.djeno.ipnexignbootcamp2025.persistence.converters;

import com.djeno.ipnexignbootcamp2025.persistence.enums.CallType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CallTypeConverter implements AttributeConverter<CallType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CallType callType) {
        if (callType == null) {
            return null;
        }
        return callType.getCode();
    }

    @Override
    public CallType convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return CallType.fromCode(code);
    }
}
