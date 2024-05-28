package tech.bread.solt.doctornyangserver.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DosageTimes {
    BREAKFAST("아침"),
    LUNCH("점심"),
    DINNER("저녁"),
    BEFORE_SLEEP("자기 전");

    private final String desc;

    public static DosageTimes ofOrdinal(Integer ordinal){
        return Arrays.stream(DosageTimes.values())
                .filter(v -> v.ordinal() == ordinal)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format("시간대에 %d가 존재하지 않음.", ordinal)));
    }
}
