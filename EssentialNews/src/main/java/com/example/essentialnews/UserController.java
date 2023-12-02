package com.example.essentialnews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    //일단 회원가입 페이지가 없기 때문에 회원 등록하는 방법으로 임시 사용
    @GetMapping("/createUser")
    @ResponseBody
    public String inputUser(@RequestParam String username, @RequestParam String password){
        Userinfo user = Userinfo.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        userRepository.save(user);
        return user.toString();
    }


}
