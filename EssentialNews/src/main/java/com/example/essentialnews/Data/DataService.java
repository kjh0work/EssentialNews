package com.example.essentialnews.Data;

import com.example.essentialnews.User.UserRepository;
import com.example.essentialnews.User.Userinfo;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
    @Value("${naver.ClientID}")
    private String clientId;

    @Value("${naver.ClientSecret}")
    private String clientSecret;

    private final DatalabRepository datalabRepository;
    private final UserRepository userRepository;

    ResponseEntity<String> result;
    public Datalab_info setDatalab_info(Datalab_info datalabInfo, String keywords_tmp, String ages_tmp) {
        List<String> keywords_list = new ArrayList<>();
        List<String> ages_list = new ArrayList<>();
        // 사용자가 입력한 키워드 문자열을 Array로 변환
        try {
            //keyword
            if (keywords_tmp != null) {
                String[] keywordTokens = keywords_tmp.split(",");
                for (String keyword : keywordTokens) {
                    if (!keyword.trim().isEmpty()) {
                        keywords_list.add(keyword.trim());
                    }
                }
            }
            //age
            if (ages_tmp != null) {
                String[] agesTokens = ages_tmp.split(",");
                for (String age : agesTokens) {
                    if (!age.trim().isEmpty()) {
                        ages_list.add(age.trim());
                    }
                }
            }
            datalabInfo.setKeywords(keywords_list);
            datalabInfo.setAge(ages_list);
        } catch (Exception e) {

        }
        return datalabInfo;

    }

    public void saveDatalab(Datalab_info datalab, String name) {
        Userinfo userinfo = userRepository.findByUsername(name);

        datalab.setUserinfo(userinfo);
        datalabRepository.save(datalab);
    }

    public Datalab_info getDatalab(String name) {
        Userinfo userinfo = userRepository.findByUsername(name);
        List<Datalab_info> datalabInfoList = datalabRepository.findByUserinfo_Id(userinfo.getId());
        if (!datalabInfoList.isEmpty()) {
            // 리스트의 마지막 요소 반환
            return datalabInfoList.get(datalabInfoList.size() - 1);
        } else {
            // 리스트가 비어있다면 null 반환
            return null;
        }
    }

    public List<Datalab_info> getDatalabAll(String name) {
        Userinfo userinfo = userRepository.findByUsername(name);
        List<Datalab_info> datalabInfoList = datalabRepository.findByUserinfo_Id(userinfo.getId());

        return datalabInfoList;
    }

    public ResponseEntity<String> getDatalabApi(Datalab_info datalab_info) {
        RestTemplate restTemplate = new RestTemplate();

        //keyword관련 JSON변환 작업
        JSONArray array = new JSONArray();
        //---------------------------------------
        JSONObject jsonObject = new JSONObject();

        String groupName = datalab_info.getKeywordGroupName();
        jsonObject.put("groupName",groupName);

        JSONArray array_keyword = new JSONArray();

        List<String> keywords = datalab_info.getKeywords();
        for(String keyword : keywords){
            array_keyword.put(keyword);
        }
        jsonObject.put("keywords",array_keyword);

        array.put(jsonObject);

        String age_array[] = datalab_info.getAge().toArray(new String[0]);

        JSONObject requestBody = new JSONObject();
        requestBody.put("startDate", datalab_info.getStartDate());
        requestBody.put("endDate", datalab_info.getEndDate());
        requestBody.put("timeUnit", datalab_info.getTimeUnit());
        requestBody.put("keywordGroups", array);
        requestBody.put("device", datalab_info.getDevice());
        requestBody.put("gender", datalab_info.getGender());
        requestBody.put("ages", age_array);


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

        result = restTemplate.exchange(req, String.class);
        return result;
    }

    public ResponseEntity<String> getNewsApi(String title) {

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

        result = restTemplate.exchange(req, String.class);
        return result;
    }
}
