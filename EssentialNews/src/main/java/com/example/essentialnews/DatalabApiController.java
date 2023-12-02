package com.example.essentialnews;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@RestController
public class DatalabApiController {
    @Value("${naver.ClientID}")
    private String clientId;

    @Value("${naver.ClientSecret}")
    private String clientSecret;


    @GetMapping("/datalab")
    public String getTrend(){

        RestTemplate restTemplate = new RestTemplate();

        JSONArray array = new JSONArray();
        //---------------------------------------
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("groupName","한글");

        JSONArray array_key1 = new JSONArray();
        array_key1.put("한글");
        array_key1.put("korean");

        jsonObject1.put("keywords",array_key1);
        //-------------------------------------
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("groupName","영어");

        JSONArray array_key2 = new JSONArray();
        array_key2.put("영어");
        array_key2.put("english");

        jsonObject2.put("keywords",array_key2);
        //-------------------------------------
        array.put(jsonObject1);
        array.put(jsonObject2);

        String age_array[] = {"4"};

        JSONObject requestBody = new JSONObject();
        requestBody.put("startDate", "2017-01-01");
        requestBody.put("endDate", "2017-04-30");
        requestBody.put("timeUnit", "month");
        requestBody.put("keywordGroups", array);
        requestBody.put("device", "pc");
        requestBody.put("gender", "m");
        requestBody.put("ages", new JSONArray(Arrays.asList("4")));


        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/datalab/search")
                .encode().build().toUri();

        RequestEntity<String> req = RequestEntity
                .post(uri)
                .header("X-Naver-Client-Id",clientId)
                .header("X-Naver-Client-Secret",clientSecret)
                .header("Content-Type", "application/json")
                .body(requestBody.toString());
                //.build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);


        return result.getBody();
    }

}
