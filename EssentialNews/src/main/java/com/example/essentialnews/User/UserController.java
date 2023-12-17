package com.example.essentialnews.User;

import com.example.essentialnews.User.UserService;
import com.example.essentialnews.User.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/welcome")
    public String welcome(){
        return "/Welcome";
    }

    @GetMapping("/login-con")
    public String loginpage(){
        return "redirect:/login";
    }

    @GetMapping("/sign-up")
    public String signup(Model model){
        model.addAttribute("userinfo",new Userinfo());
        return "/SignupPage";
    }

    @PostMapping("/sign-up")
    public String signupSubmit(@ModelAttribute("userinfo") Userinfo userinfo, Model model){
        boolean isUsernameOk = userService.checkDuplicate(userinfo.getUsername());
        if(!isUsernameOk){
            model.addAttribute("usernameDuplicate", "이미 존재하는 사용자명입니다.");
            model.addAttribute("userinfo", userinfo);
            return "/SignupPage";
        }

        userService.saveUser(userinfo.getUsername(), userinfo.getPassword());
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String SuccessLogin(){
        return "/PersonalPage";
    }

    @GetMapping("/homepage")
    public String homepage(Model model){

        return "/PersonalPage";
    }

    @PostMapping("/logout")
    public String goTowelcome(){

        return "/Welcome";
    }






}
