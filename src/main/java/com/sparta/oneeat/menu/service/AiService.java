package com.sparta.oneeat.menu.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.menu.dto.request.AiCallRequestDto;
import com.sparta.oneeat.menu.dto.request.AiRequestDto;
import com.sparta.oneeat.menu.dto.response.AiResponseDto;
import com.sparta.oneeat.menu.entity.Ai;
import com.sparta.oneeat.menu.repository.AiRepository;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.repository.StoreRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class AiService {

    private final RestTemplate restTemplate;
    private final String API_KEY;
    private final AiRepository aiRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public AiService(RestTemplateBuilder builder, @Value("${api.key}") String apiKey,
        AiRepository aiRepository, UserRepository userRepository, StoreRepository storeRepository) {
        this.restTemplate = builder.build();
        this.API_KEY = apiKey;
        this.aiRepository = aiRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    // ai 요청이 들어오면 질문 조합
    public AiResponseDto generateQuestion(long userID, AiCallRequestDto requestDto) {
        String category = validationAndGetStoreCategory(userID, requestDto.getStoreId());

        String question = "메뉴 이름 : " + requestDto.getMenuName() + "카테고리 : " + category
            + " 를 가지고 해당 메뉴에 대한 설명을 만들어줘. 답변을 최대한 간결하게 50자 이하로";

        return aiApiCall(question);
    }

    // ai api 호출
    private AiResponseDto aiApiCall(String question) {
        // URI 생성
        URI uri = generateUri();
        log.info("uri {}", uri);
        log.info("question : {}", question);

        // 본문 생성
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(Map.of("text", question)))
            )
        );

        RequestEntity<Map<String, Object>> requestEntity = RequestEntity
            .post(uri)
            .header("Content-Type", "application/json")
            .body(requestBody);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity,
                String.class);
            log.info("status code {}", responseEntity.getStatusCode());
            return toDto(question, responseEntity.getBody());
        } catch (HttpClientErrorException e) {

            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new CustomException(ExceptionType.AI_ACCESS_DENIED); // 인증키
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CustomException(ExceptionType.NOT_FOUND); // 리소스 없음
            } else {
                throw new CustomException(ExceptionType.INVALID_REQUEST);
            }

        } catch (HttpServerErrorException e) {
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR);
        }
    }

    // ai api 호출을 위한 URL 생성
    private URI generateUri() {
        return UriComponentsBuilder
            .fromUriString("https://generativelanguage.googleapis.com")
            .path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
            .queryParam("key", API_KEY)
            .encode()
            .build()
            .toUri();
    }

    // ai api 에서 응답받은 데이터 반환
    public AiResponseDto toDto(String question, String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);

        // "candidates" 배열에서 첫 번째 항목을 가져옴
        JSONObject candidate = jsonObject.getJSONArray("candidates").getJSONObject(0);

        // "content" -> "parts" -> "text" 값을 추출
        String response = candidate.getJSONObject("content").getJSONArray("parts").getJSONObject(0)
            .getString("text");

        log.info("response : {}", response);

        return new AiResponseDto(question, response);
    }

    // ai 데이터 저장
    public void saveAi(AiRequestDto aiRequestDto) {
        // 메뉴 유효성
        // 유저 유효성
        if(!userRepository.existsById(aiRequestDto.getUserId())) {
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); // 유저 없음
        }

        log.info("aiRequest : {}", aiRequestDto);
        Ai ai = new Ai(aiRequestDto);
        aiRepository.save(ai);
    }

    private String validationAndGetStoreCategory(long userId, UUID storeId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 유저 없음

        List<Store> stores = storeRepository.findAllByUser(user);

        // 해당 storeId와 일치하는 가게 찾기
        Store store = stores.stream()
            .filter(s -> s.getId().equals(storeId))
            .findFirst()
            .orElseThrow(
                () -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 일치하는 가게 없음

        log.info(store.getCategory().getCategory().name());

        // 해당 가게의 카테고리 반환
        return store.getCategory().getCategory().name();
    }

}
