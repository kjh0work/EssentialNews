package com.example.essentialnews.User;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    public void saveUser(String username, String password){
        Userinfo user = Userinfo.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        userRepository.save(user);
    }


    public boolean checkDuplicate(String username) {
        if(userRepository.findByUsername(username) != null){
            return false;
        }
        return true;
    }
}
