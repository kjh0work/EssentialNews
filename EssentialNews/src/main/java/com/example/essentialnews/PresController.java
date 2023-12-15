package com.example.essentialnews;

import com.example.essentialnews.Datalab.Datalab_input_info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PresController {


    Datalab_input_info datalabInputInfo = new Datalab_input_info();

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
