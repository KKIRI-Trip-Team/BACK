package com.kkiri_trip.back.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TierLevel {
    Treseed("트리시드", 0, 29),
    Treker("트레커", 30, 59),
    Treveller("트래블러", 60, 79),
    Tregionary("트리저너리", 80, 94),
    Trelegend("트레전드", 95, 100);

    private final String name;
    private final long min;
    private final long max;

    public static TierLevel fromScore(long score){
        return Arrays.stream(values())
                .filter(tierLevel -> score >= tierLevel.min && score <= tierLevel.max)
                .findFirst()
                .get();
    }

}
