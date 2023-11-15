package com.goldmen.home.house.trade;

import com.goldmen.home.house.global.domain.HouseInfo;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Trade {
    @Id
    @Column(name = "trade_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private HouseInfo houseInfo;

    private int price;
}