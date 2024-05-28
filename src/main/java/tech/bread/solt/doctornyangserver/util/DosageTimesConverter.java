package tech.bread.solt.doctornyangserver.util;

import jakarta.persistence.AttributeConverter;

public class DosageTimesConverter implements AttributeConverter<DosageTimes, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DosageTimes dosageTimes) {
        return dosageTimes.ordinal();
    }

    @Override
    public DosageTimes convertToEntityAttribute(Integer dbData) {
        return DosageTimes.ofOrdinal(dbData);
    }
}
