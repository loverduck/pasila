package org.ssafy.pasila.global.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * RestTemplate을 생성하고 반환하는 클래스.
 * RestTemplate 헤더를 설정해줌(API 인증을 위해)
 */
@Configuration
public class OpenAIRestTemplateConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Bean
    @Qualifier("openaiRestTemplate")
    public RestTemplate openaiRestTemplate() {

        RestTemplate restTemplate = new RestTemplate(); //인스턴스 생성
        restTemplate.getInterceptors().add((request, body, execution) -> { //인증
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body); //실제 http 요청을 실행하도록 객체에게 제어 전달
        });
        return restTemplate;

    }
}