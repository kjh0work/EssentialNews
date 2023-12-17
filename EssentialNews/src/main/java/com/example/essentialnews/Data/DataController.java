package com.example.essentialnews.Data;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DataController {

    @Autowired
    DataService dataService;
    ResponseEntity<String> datalab_result;
    ResponseEntity<String> news_result;
    String title_tmp;

    //사용자에게 키워드 정보 입력받기
    @GetMapping("/keyword")
    public String input_keyword(Model model){
        model.addAttribute("datalab",new Datalab_info());
        return "datalab_keyword";
    }

    //사용자의 이전 기록 보기
    @GetMapping("/history")
    public String showHistory(Model model, Principal principal){
        List<Datalab_info> Datalab_list = dataService.getDatalabAll(principal.getName());
        model.addAttribute("datalab_list", Datalab_list);
        return "/history";
    }

    //사용자에게 Datalab api 관련 정보 받아오기
    @PostMapping("/get-api")
    public String getApi(@ModelAttribute("datalab") Datalab_info datalabInfo,
                         @RequestParam("keywords_input") String keywords_tmp,
                         @RequestParam("ages") String ages_tmp, Principal principal) {
        //사용자에게 받아온 정보 정리
        Datalab_info datalab = dataService.setDatalab_info(datalabInfo, keywords_tmp, ages_tmp);
        //DB에 저장
        dataService.saveDatalab(datalab, principal.getName());
        //저장된 정보를 바탕으로 Datalab api를 받아오는 로직으로 redirect
        return "redirect:/get_datalab_api";
    }

    //실제 api 받아오기
    @GetMapping("/get_datalab_api")
    public String getDatalab(Principal principal){
        //사용자의 키워드 정보를 담고 있는 객체
        Datalab_info datalab_info = dataService.getDatalab(principal.getName());
        //api 응답을 result로 받기
        datalab_result = dataService.getDatalabApi(datalab_info);
        //받은 응답을 사용자에게 보여주는 로직으로 redirect
        return "redirect:/show-graph";
    }

    //datalab api 사용자에게 보여주기
    @GetMapping("/show-graph")
    public String showGraph(Model model) {
        ResponseEntity<String> response = datalab_result;
        String apiResponse = response.getBody();

        //apiResponse를 파승해서 view에 전달
        JSONObject jsonObject = new JSONObject(apiResponse);
        JSONArray results_array = jsonObject.getJSONArray("results");

        //results는 길이가 1인 배열, JSONobject가 들어있다.
        JSONObject object = results_array.getJSONObject(0);

        //title, 주제어 가져오기
        String title = object.getString("title");
        JSONArray data_array = object.getJSONArray("data");

        String[] period = new String[data_array.length()];
        for(int i = 0;i<data_array.length();i++){
            JSONObject tmp = data_array.getJSONObject(i);
            period[i] = tmp.getString("period");
        }

        Double[] ratio = new Double[data_array.length()];
        for(int i = 0;i<data_array.length();i++){
            JSONObject tmp = data_array.getJSONObject(i);
            ratio[i] = tmp.getDouble("ratio");
        }

        // API 응답을 모델에 추가
        model.addAttribute("title", title);
        model.addAttribute("period", period);
        model.addAttribute("ratio", ratio);

        return "graphView";
    }
    //news api받아오기
    @PostMapping("/get_news_api")
    public String getNewsApi(@RequestParam("title") String title){
        //news api 받아오기
        news_result = dataService.getNewsApi(title);
        title_tmp = title;
        return "redirect:/news_api_cal";
    }

    //받아온 news정보 사용자에게 보여주기
    @GetMapping("/news_api_cal")
    public String NewApi(Model model){
        ResponseEntity<String> response = news_result;
        String news = response.getBody();

        JSONObject jsonObject = new JSONObject(news);
        JSONArray items_array = jsonObject.getJSONArray("items");

        List<News_info> newsitem_list = new ArrayList<>();

        for(int i = 0;i<items_array.length();i++){
            JSONObject object = items_array.getJSONObject(i);

            String title = object.getString("title");
            String link = object.getString("link");
            String pubDate = object.getString("pubDate");

            newsitem_list.add(new News_info(title, link, pubDate));
        }

        model.addAttribute("newsitems", newsitem_list);
        model.addAttribute("keyword", title_tmp);
        return "/show-news-response";
    }
}
