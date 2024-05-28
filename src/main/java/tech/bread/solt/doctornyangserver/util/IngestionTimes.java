package tech.bread.solt.doctornyangserver.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
@AllArgsConstructor
@Getter
public enum IngestionTimes {
    BREAKFAST("아침"),
    LUNCH("점심"),
    DINNER("저녁"),
    SNACK("간식");

    private final String desc;

    public static IngestionTimes ofOrdinal(Integer ordinal){
        return Arrays.stream(IngestionTimes.values())
                .filter(v -> v.ordinal() == ordinal)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format("시간대에 %d가 존재하지 않음.", ordinal)));
    }
}
