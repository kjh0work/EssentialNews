package com.example.essentialnews.User;


import com.example.essentialnews.Datalab.Datalab_input_info;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
//@Table
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Userinfo {

    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(mappedBy = "userinfo")
    private List<Datalab_input_info> datalabInputInfos;

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
