package tech.bread.solt.doctornyangserver.util;

import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;


public class IngestionTimesConverter implements AttributeConverter<IngestionTimes, Integer> {
    @Override
    public Integer convertToDatabaseColumn(IngestionTimes dosageTimes) {
        return dosageTimes.ordinal();
    }

    @Override
    public IngestionTimes convertToEntityAttribute(Integer dbData) {
        return IngestionTimes.ofOrdinal(dbData);
    }
}
