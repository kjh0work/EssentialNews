package com.example.essentialnews.Datalab;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DatalabApiController {
    @Value("${naver.ClientID}")
    private String clientId;

    @Value("${naver.ClientSecret}")
    private String clientSecret;

    private final DatalabRepository datalabRepository;
    private final DatalabResponseRepository datalabResponseRepository;

    Datalab_input_info datalab_instance;
    ResponseEntity<String> result;

    @PostMapping("/get-api")
    public String getApi(@ModelAttribute("datalab") Datalab_input_info datalabInputInfo,
                         @RequestParam("keywords_input") String keywords_tmp,
                         @RequestParam("ages") String ages_tmp) {
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
            datalabInputInfo.setKeywords(keywords_list);
            datalabInputInfo.setAge(ages_list);
        } catch (Exception e) {

        }

        datalabRepository.save(datalabInputInfo);
        datalab_instance = datalabInputInfo;
       return "redirect:/get_datalab_api";
    }

    @GetMapping("/get_datalab_api")
    public String getDatalab(){

        RestTemplate restTemplate = new RestTemplate();

        //keyword관련 JSON변환 작업
        JSONArray array = new JSONArray();
        //---------------------------------------
        JSONObject jsonObject = new JSONObject();

        String groupName = datalab_instance.getKeywordGroupName();
        jsonObject.put("groupName",groupName);

        JSONArray array_keyword = new JSONArray();

        List<String> keywords = datalab_instance.getKeywords();
        for(String keyword : keywords){
            array_keyword.put(keyword);
        }
        jsonObject.put("keywords",array_keyword);

        array.put(jsonObject);

        String age_array[] = datalab_instance.getAge().toArray(new String[0]);

        JSONObject requestBody = new JSONObject();
        requestBody.put("startDate", datalab_instance.getStartDate());
        requestBody.put("endDate", datalab_instance.getEndDate());
        requestBody.put("timeUnit", datalab_instance.getTimeUnit());
        requestBody.put("keywordGroups", array);
        requestBody.put("device", datalab_instance.getDevice());
        requestBody.put("gender", datalab_instance.getGender());
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

        return "redirect:/show-graph";
    }

    @GetMapping("/show-graph")
    public String showGraph(Model model) {
        ResponseEntity<String> response = result;
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
}
