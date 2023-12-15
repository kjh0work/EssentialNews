package com.example.essentialnews.Datalab;

import com.example.essentialnews.User.Userinfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Datalab_input_info {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userinfo userinfo;

    private String startDate;
    private String endDate;
    private String timeUnit;

    private String keywordGroupName;

    @ElementCollection
    @CollectionTable(name = "datalab_keywords", joinColumns = @JoinColumn(name = "datalab_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    private String device;
    private String gender;

    @ElementCollection
    @CollectionTable(name = "datalab_ages", joinColumns = @JoinColumn(name = "datalab_id"))
    @Column(name = "age")
    private List<String> age;

    @Override
    public String toString() {
        return "Datalab_info{" +
                "startDAte='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", timeUnit='" + timeUnit + '\'' +
                ", keywordGroupName=" + keywordGroupName +
                ", keywords=" + keywords.toString() +
                ", device='" + device + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age.toString() +
                '}';
    }
}
