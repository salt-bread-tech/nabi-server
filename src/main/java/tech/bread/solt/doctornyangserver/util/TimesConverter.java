package tech.bread.solt.doctornyangserver.util;

import jakarta.persistence.AttributeConverter;

public class TimesConverter implements AttributeConverter<Times, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Times times) {
        return times.ordinal();
    }

    @Override
    public Times convertToEntityAttribute(Integer dbData) {
        return Times.ofOrdinal(dbData);
    }
}
