package com.ssafy.self.chat.controller;

import com.ssafy.self.chat.model.ChatRequest;
import com.ssafy.self.chat.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.util.StopWatch;

@Controller
@RequestMapping("/chat2")
@Slf4j
public class ChatController2 {

    @Qualifier("openaiWebClient")
    @Autowired
    private WebClient webClient;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @GetMapping
    @ResponseBody
    public String chat() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();

        // create a request
        String prompt = "webClient를 사용하는 이유에 대해 한문장으로 알려주세요.";
        ChatRequest request = new ChatRequest(model, prompt);

        // call the API using WebClient
        try {
            ChatResponse response = webClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block(); // blocking call to wait for the response


            System.out.println("chat response: {}"+ response.getChoices().get(0).getMessage().getContent());
            if (response.getChoices() == null || response.getChoices().isEmpty()) {
                return "No response";
            }

//            stopWatch.stop();
//            System.out.println("stopWatch: {}"+ stopWatch.prettyPrint());

            // return the first response
            return response.getChoices().get(0).getMessage().getContent();

        } catch (Exception e) {
            log.error("Error occurred while making the API request: {}", e.getMessage());
        }

        return prompt;
    }
}
