package com.sparta.oneeat.store.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class GeocodingService {
    private static final Logger logger = LoggerFactory.getLogger(GeocodingService.class);

    @Value("${kakao.api.key}")
    private String apiKey;

    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";

    public JSONObject getCoordinates(String address) throws Exception {

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

//        logger.info("Kakao API request URL: {}", url);
//        logger.info("Kakao API request headers: {}", headers);
//        logger.info("Kakao API response for address '{}': {}", address, response);


        JSONObject jsonResponse = new JSONObject(response);

        return jsonResponse.getJSONArray("documents").getJSONObject(0);

    }
}