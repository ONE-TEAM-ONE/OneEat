package com.sparta.oneeat.common.env;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnvTest {

    private static Dotenv dotenv;

    @Test
    @Order(1)
    @DisplayName("1. .env 파일 로드")
    void loadEnv() {
        assertDoesNotThrow(() -> {
            dotenv = Dotenv.load();
        }, "DotenvException 이 발생하지 않아야 합니다.");

        assertNotNull(dotenv, "dotenv 객체가 null 이 아닙니다.");
    }

    @Test
    @Order(2)
    @DisplayName("2. DB 데이터 확인")
    void checkDbEnvVars() {
        assertNotNull(dotenv, ".env 파일이 로드되지 않았습니다. 먼저 'loadEnv' 테스트가 통과해야 합니다.");

        assertNotNull(dotenv.get("DB_URL"), "DB_URL 값이 존재하지 않습니다.");
        assertNotNull(dotenv.get("DB_USERNAME"), "DB_USERNAME 값이 존재하지 않습니다.");
        assertNotNull(dotenv.get("DB_PASSWORD"), "DB_PASSWORD 값이 존재하지 않습니다.");
    }

    @Test
    @Order(3)
    @DisplayName("3. JWT 데이터 확인")
    void checkJwtEnvVars() {
        assertNotNull(dotenv, ".env 파일이 로드되지 않았습니다. 먼저 'loadEnv' 테스트가 통과해야 합니다.");

        assertNotNull(dotenv.get("jwt.secret_key"), "Secret Key가 존재하지 않습니다.");
    }

    @Test
    @Order(4)
    @DisplayName("4. API key 확인")
    void checkApiKey() {
        assertNotNull(dotenv.get("API_KEY"), "API_KEY 값이 존재하지 않습니다.");
    }

}
