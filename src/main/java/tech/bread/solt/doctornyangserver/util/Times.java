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
    DURING_BREAKFAST("아침 식중"),
    AFTER_BREAKFAST("아침 식후"),
    BEFORE_LUNCH("점심 식전"),
    DURING_LUNCH("점심 식중"),
    AFTER_LUNCH("점심 식후"),
    BEFORE_DINNER("저녁 식전"),
    DURING_DINNER("저녁 식중"),
    AFTER_DINNER("저녁 식후"),
    BEFORE_SLEEP("자기 전"),
    EMPTY_STOMACH("공복"),
    BREAKFAST("아침 식사"),
    LUNCH("점심 식사"),
    DINNER("저녁 식사"),
    SNACK("간식");

    private final String desc;

    public static Times ofOrdinal(Integer ordinal){
        return Arrays.stream(Times.values())
                .filter(v -> v.ordinal() == ordinal)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format("시간대에 %d가 존재하지 않음.", ordinal)));
    }
}
