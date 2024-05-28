package tech.bread.solt.doctornyangserver.util;

import jakarta.persistence.AttributeConverter;

public class TakingDosagesConverter implements AttributeConverter<TakingDosages, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TakingDosages takings) {
        return takings.ordinal();
    }

    @Override
    public TakingDosages convertToEntityAttribute(Integer dbData) {
        return TakingDosages.ofOrdinal(dbData);
    }
}