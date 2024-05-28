package tech.bread.solt.doctornyangserver.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TakingDosages {
    BEFORE("식전"),
    DURING("식중"),
    AFTER("식후"),
    ANYTIME("상관 없음");

    private final String desc;

    public static TakingDosages ofOrdinal(Integer ordinal){
        return Arrays.stream(TakingDosages.values())
                .filter(v -> v.ordinal() == ordinal)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format("복용 방법에 %d가 존재하지 않음.", ordinal)));
    }
}
