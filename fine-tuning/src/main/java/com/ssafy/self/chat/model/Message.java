package com.ssafy.self.chat.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

//    @JsonProperty("content")
    private String content;

//    @JsonProperty("role")
    private String role;

    public Message(String role, String prompt) {
        this.role = role;
        this.content = prompt;
    }

}
