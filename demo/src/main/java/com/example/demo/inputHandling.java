package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class inputHandling {

    private String convertMapToJson(Map<Integer, Object> map) {
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the Map to a JSON string
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // Handle the exception if the conversion fails
            e.printStackTrace(); // You might want to log the error or throw a custom exception
            return "{\"error\": \"Failed to convert Map to JSON\"}";
        }
    }

    @PostMapping("/calculate") //Structure: 
    public String getInput(@RequestBody Map<Integer, Object> newMapData) {
        Map<Integer, Object> resultMap = new HashMap<>();
        resultMap.put(1, true);
        for(int i = 1; i <= newMapData.size(); i++){
            if(newMapData.get(i) == ""){
                resultMap.put(1, false);
                break;
            }
        }
        
        int laps = Integer.parseInt((String) newMapData.get(1));
        int tL = Integer.parseInt((String) newMapData.get(2));
        int noS = Integer.parseInt((String) newMapData.get(3));
        int noM = Integer.parseInt((String) newMapData.get(4));
        int noH = Integer.parseInt((String) newMapData.get(5));
        double eQLS = Double.parseDouble((String) newMapData.get(7));
        double eQLM = Double.parseDouble((String) newMapData.get(8));
        double eQLH = Double.parseDouble((String) newMapData.get(9));
        int mLS = Integer.parseInt((String) newMapData.get(9));
        int mLM = Integer.parseInt((String) newMapData.get(10));
        int mLH = Integer.parseInt((String) newMapData.get(11));
        double eTLS = Double.parseDouble((String) newMapData.get(12));
        double eTLM = Double.parseDouble((String) newMapData.get(13));
        double eTLH = Double.parseDouble((String) newMapData.get(14));
        double lTS = Double.parseDouble((String) newMapData.get(15));
        double lTM = Double.parseDouble((String) newMapData.get(16));
        double lTH = Double.parseDouble((String) newMapData.get(17));
        createStategy newC = new createStategy(laps, tL, noS, noM, noH, eQLS, eQLM, eQLH, mLS, mLM, mLH, eTLS, eTLM, eTLH, lTS, lTM, lTH);
        ArrayList<String> result = new ArrayList<>();
        result = newC.calculateStrategy(1000);
        for(int i = 0; i < result.size(); i++){
            resultMap.put(i+2, result.get(i));
        }
        String jsonResponse = convertMapToJson(resultMap);
        return jsonResponse;
    }
    

}
