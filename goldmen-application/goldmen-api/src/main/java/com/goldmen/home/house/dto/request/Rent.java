package com.goldmen.home.house.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Rent{

    private Integer max;

    private Integer min;

    public Rent(@JsonProperty("max") Integer max, @JsonProperty("min") Integer min){
        this.max = max;
        this.min = min;
    }

    public Rent() {

    }
}