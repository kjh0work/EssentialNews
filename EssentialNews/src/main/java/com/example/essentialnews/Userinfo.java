package com.example.essentialnews;


import jakarta.persistence.*;
import lombok.*;


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

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
