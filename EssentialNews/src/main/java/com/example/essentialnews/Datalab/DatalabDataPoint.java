package com.example.essentialnews.Datalab;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatalabDataPoint {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_response_id")
    private Datalab_api_response apiResponse;

    private LocalDate period; // LocalDate는 날짜를 나타내는 자바 8 이상의 타입입니다.
    private Double ratio;

}
