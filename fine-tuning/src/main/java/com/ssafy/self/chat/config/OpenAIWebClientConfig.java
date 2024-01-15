package com.ssafy.self.chat.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient를 생성하고 반환하는 클래스.
 * WebClient 헤더를 설정해줌(API 인증을 위해)
 */
@Configuration
public class OpenAIWebClientConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Bean
    @Qualifier("openaiWebClient")
    public WebClient openaiWebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector()) // Reactor-based HTTP connector
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // Set maxInMemorySize to 16MB
                        .build())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .build();
    }
}
