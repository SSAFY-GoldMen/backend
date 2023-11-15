package com.goldmen.home.district.domain;

import com.goldmen.home.legal.domain.Legal;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class District {
    @Id
    @Column(name = "district_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Max(10)
    @Column(name = "district_name", nullable = false, length = 10)
    private String name;

    @Max(10)
    @Column(name = "district_code", nullable = false, length = 10)
    private String code;

    @OneToMany(mappedBy = "district")
    private List<Legal> legals = new ArrayList<>();
}