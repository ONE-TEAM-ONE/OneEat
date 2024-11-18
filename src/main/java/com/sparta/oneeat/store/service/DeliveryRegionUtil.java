package com.sparta.oneeat.store.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DeliveryRegionUtil {

    @Value("${kakao.api.key}")
    private String apiKey;

    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";

    public String getKakaoApiFromAddress(String address) throws Exception {

        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = API_URL + encodedAddress;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", entity.getHeaders().getFirst("Authorization"));

        InputStream inputStream = conn.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String response = new String(bytes, StandardCharsets.UTF_8);

        conn.disconnect();
        return response;
    }

    public String getFormattedAddress(String address) {
        try{
            String response = getKakaoApiFromAddress(address);

            JSONObject jsonResponse = new JSONObject(response);
            JSONObject meta = jsonResponse.getJSONObject("meta");
            if(!jsonResponse.has("meta")){
                return "";
            }
            int totalCount = meta.getInt("total_count");
            if (totalCount == 0) {
                return "";
            }
            JSONObject document = jsonResponse.getJSONArray("documents").getJSONObject(0);

            JSONObject address_obj = document.getJSONObject("address");
            String region1 = address_obj.getString("region_1depth_name");
            String region2 = address_obj.getString("region_2depth_name");
            String region3 = address_obj.getString("region_3depth_name");

            String deliveryRegion = region1 + " " + region2 + " " + region3;

            return deliveryRegion;

        }catch (Exception e) {
            return "";
        }
    }
}