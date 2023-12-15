package com.example.essentialnews.News;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class NewsApiController {
    @Value("${naver.ClientID}")
    private String clientId;

    @Value("${naver.ClientSecret}")
    private String clientSecret;

    @PostMapping("/get_news_api")
    public String getNewsApi(@ModelAttribute("title") String title,
                             @ModelAttribute("period") String[] period,
                             @ModelAttribute("ratio") Double[] ratio){




        return "";
    }

    @GetMapping("/news")
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
    }



}
