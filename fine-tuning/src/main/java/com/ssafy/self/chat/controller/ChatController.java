package com.ssafy.self.chat.controller;

import com.ssafy.self.chat.model.ChatRequest;
import com.ssafy.self.chat.model.ChatResponse;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.finetune.FineTuneRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@RestController
@Slf4j
public class ChatController {

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.fine-tuning}")
    private String fineTuningUrl;

    @PostMapping("/fine")
    public ResponseEntity<?> fineTuning(){
        OpenAiService service =new OpenAiService("${openai.api.key}");
        CompletionRequest request = new CompletionRequest();
        request.setModel("ftjob-Gt5StA8LabB5GX1dqBAuZ4sa");
        request.setPrompt("기분이 좋아요");
        CompletionResult result = service.createFineTuneCompletion(request);


        return ResponseEntity.ok(result.getChoices().get(0).getText());
    }


    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {

//        prompt += "에 대해 200자 이내로 설명해주세요 알려주세요.";
        log.info("prompt: {}", prompt);
        ChatRequest request = new ChatRequest(model, prompt);
        log.info("chat request: {}",request.getMessages().get(0).toString());
        // call the API
        try {
            ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
            assert response != null;
//            System.out.println("chat response: {}"+ response.getChoices().get(0).getMessage().getContent());

            if (response.getChoices() == null || response.getChoices().isEmpty()) {
                return "No response";
            }

            // return the first response
            return response.getChoices().get(0).getMessage().getContent();

        } catch (RestClientException e) {
            log.error("Error occurred while making the API request: {}", e.getMessage());
        }

//        stopWatch.stop();
//        System.out.println(stopWatch.prettyPrint());

        return prompt;
    }


}