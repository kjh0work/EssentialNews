package com.example.essentialnews.User;

import com.example.essentialnews.User.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Userinfo, Long> {

    Userinfo findByUsername(String username);
}
