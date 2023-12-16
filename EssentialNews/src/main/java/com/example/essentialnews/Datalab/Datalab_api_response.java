package com.example.essentialnews.Datalab;

import com.example.essentialnews.User.Userinfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Datalab_api_response {

    @Id @GeneratedValue
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "datalab_input_info_id")
    private Datalab_input_info datalabInputInfo;

    private String title;

    @OneToMany(mappedBy = "apiResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DatalabDataPoint> dataPoints = new ArrayList<>();
}
