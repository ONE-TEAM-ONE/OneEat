package com.sparta.oneeat.common.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AddressService {

    @Value("${kakao.api.key}")
    private String apiKey;

    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";

    public ResponseEntity<String> getKakaoApiFromAddress(String address) {
        try {
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
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<String[]> getFormattedAddress(String address) {
        try {
            ResponseEntity<String> response = getKakaoApiFromAddress(address);

            if (response.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(response.getStatusCode()).body(new String[]{"주소를 찾지 못함"});
            }

            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONObject document = jsonResponse.getJSONArray("documents").getJSONObject(0);

            JSONObject roadAddressObj = document.getJSONObject("road_address");
            String roadAddress = roadAddressObj.getString("address_name");

            JSONObject address_obj = document.getJSONObject("address");
            String region1 = address_obj.getString("region_1depth_name");
            String region2 = address_obj.getString("region_2depth_name");
            String region3 = address_obj.getString("region_3depth_name");

            String fullAddress = region1 + " " + region2 + " " + region3;

            return ResponseEntity.ok(new String[]{roadAddress, fullAddress});

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new String[]{"주소를 찾지 못함"});
        }
    }
}