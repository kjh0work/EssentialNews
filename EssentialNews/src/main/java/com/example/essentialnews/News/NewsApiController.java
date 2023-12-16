package com.example.essentialnews.News;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class NewsApiController {
    @Value("${naver.ClientID}")
    private String clientId;

    @Value("${naver.ClientSecret}")
    private String clientSecret;

    ResponseEntity<String> news_result;

    @PostMapping("/get_news_api")
    public String getNewsApi(@RequestParam("title") String title,
                             @RequestParam("period") String periodString,
                             @RequestParam("ratio") String ratioString){
        System.out.println("ratioSTring : ");
        System.out.println(ratioString);
        /*String[] period = periodString.split(",");
        String[] ratioStrings = ratioString.split(",");
        Double[] ratio = new Double[ratioStrings.length];
        for (int i = 0; i < ratioStrings.length; i++) {
            ratio[i] = Double.parseDouble(ratioStrings[i]);
        }*/

        // ratio에서 가장 높은 비율 (중복이면 period가 최신) 구하기
        /*double max_ratio = 0.0;
        int max_ratio_ind = -1;
        for(int i = 0;i<ratio.length;i++){
            if(max_ratio < ratio[i]){
                max_ratio = ratio[i];
                max_ratio_ind = i;
            }
        }*/

        RestTemplate restTemplate = new RestTemplate();

        String query = title;
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", encoded)
                .queryParam("display", 100)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .build()
                .encode()
                .toUri();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id",clientId)
                .header("X-Naver-Client-Secret",clientSecret)
                .build();

        news_result = restTemplate.exchange(req, String.class);

        return "redirect:/news_api_cal";

        //Todo
        // 1. ratio에서 가장 높은 비율 (중복이면 period가 최신) 구하기
        // 2. 해당 ratio에 대한 period 저장해 놓기
        // 3. 키워드로 뉴스 받아와서 period에 해당되는 것만 남기기
        // --------------------------------------------------------
        // 4. 파승해서 필요한 정보만 뽑기
        // 5. 사용자 한테 보여주기
        // => 내일 오전까지 완료
        // 추가로 사용자 개인 페이지에서 과거에 본인이 입력한 키워드랑, 그에 따른 결과물과 뉴스를 볼 수 있는 기능 만들기
        // 이렇게 까지만 추가하고 front 꾸미고, docker에 올려서 (DB를 docker에서 Mysql실행), aws로 실행시키면 완료다.
        // => 내일 오후에 완료
        // 내가 만든 것을 토대로 보고서 작성하고 데모 영상 찍기
        // => 일요일 오전까지 보고서 완료, 저녁을 먹기 전에 제출 완료
        // 그 후 저녁부터는 운체실에 몰입해보자. 상황을 정해놓고 어떻게 할지. 조금 물리면 알고리즘 공부하고
        // 이렇게 마지막 유예기간을 보낸 후에. 알고리즘 시험이 끝난 후 나아가기 위한 rule을 다시 정리한다.


        //return "";
    }

    @GetMapping("/news_api_cal")
    public String NewApi(Model model){
        ResponseEntity<String> response = news_result;
        String news = response.getBody();

        JSONObject jsonObject = new JSONObject(news);
        JSONArray items_array = jsonObject.getJSONArray("items");

        List<Newsitems> newsitem_list = new ArrayList<>();

        for(int i = 0;i<items_array.length();i++){
            JSONObject object = items_array.getJSONObject(i);

            String title = object.getString("title");
            String link = object.getString("link");
            String pubDate = object.getString("pubDate");

            newsitem_list.add(new Newsitems(title, link, pubDate));
        }

        model.addAttribute("newsitems", newsitem_list);




        return "/show-news-response";
    }

   /* @GetMapping("/news")
    public String getNews(){
        RestTemplate restTemplate = new RestTemplate();

        String query = "한글";
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", encoded)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "date")
                .build()
                .encode()
                .toUri();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id",clientId)
                .header("X-Naver-Client-Secret",clientSecret)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        return result.getBody();
    }*/



}
