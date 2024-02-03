package tech.bread.solt.doctornyangserver.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Times {
    BEFORE_BREAKFAST("아침 식전"),
    AFTER_BREAKFAST("아침 식후"),
    BEFORE_LUNCH("점심 식전"),
    AFTER_LUNCH("점심 식후"),
    BEFORE_DINNER("저녁 식전"),
    AFTER_DINNER("저녁 식후"),
    BEFORE_SLEEP("자기 전");

    private final String desc;

    public static Times ofOrdinal(Integer ordinal){
        return Arrays.stream(Times.values())
                .filter(v -> v.ordinal() == ordinal)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format("복용시간에 %d가 존재하지 않음.", ordinal)));
    }
}
