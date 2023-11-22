package com.goldmen.home.building.Monthly.cond;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Rent{
    private final Integer max;
    private final Integer min;

    public Rent(Integer max, Integer min) {
        if (max == null) this.max = 1_000;
        else this.max = max;
        if (min == null) this.min = 0;
        else this.min = min;
    }
}
