package com.example.essentialnews;

import com.example.essentialnews.Datalab.Datalab_input_info;
import com.example.essentialnews.User.UserService;
import com.example.essentialnews.User.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PresController {

    @Autowired
    UserService userService;

    Datalab_input_info datalabInputInfo = new Datalab_input_info();

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
    public String signupSubmit(@ModelAttribute("userinfo") Userinfo userinfo){

        //Todo
        // 사용자로 부터 잘못된 입력을 받으면 validation을 통해 오류를 전달하기 추가

        userService.saveUser(userinfo.getUsername(), userinfo.getPassword());
        return "redirect:/login";
    }



    @GetMapping("/homepage")
    public String homepage(Model model){

        return "/PersonalPage";
    }

    @GetMapping("/keyword")
    public String input_keyword(Model model){
        model.addAttribute("datalab",datalabInputInfo);
        return "datalab_keyword";
    }


}
