package tech.bread.solt.doctornyangserver.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tech.bread.solt.doctornyangserver.model.GPTMessage;
import tech.bread.solt.doctornyangserver.model.dto.request.ChatGPTRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ChatGPTResponse;

import java.util.ArrayList;
import java.util.List;

public class GPTManager {
    private String apiKey;
    private String model;
    private String url;
    private List<GPTMessage> messages;
    private Integer maxToken;
    private Double temperature;
    private Double topP;
    private String authorization;
    private String bearer;
    private String mediaType;

    private static RestTemplate restTemplate = new RestTemplate();

    public GPTManager() {
        apiKey = KeySet.GPT_API_KEY.getKey();
        model = KeySet.GPT_MODEL.getKey();
        url = KeySet.GPT_URL.getKey();

        messages = new ArrayList<>();
        messages.add(GPTMessage.builder()
                .role("system")
                .content("당신은 고양이입니당")
                .build());

        messages.add(GPTMessage.builder()
                .role("system")
                .content("말 끝마다 냥을 붙여주세냥")
                .build());

        messages.add(GPTMessage.builder()
                .role("system")
                .content("심리상담사처럼 상냥하게 이야기를 들어달라냥")
                .build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("예시").build());

        maxToken = 2000;
        temperature = 0.0;
        topP = 1.0;

        authorization = "Authorization";
        bearer = "Bearer ";
        mediaType = "application/json; charset=UTF-8";
    }

    public ChatGPTResponse getResponse(String prompt) {
        messages.add(GPTMessage.builder()
                .role("user")
                .content(prompt)
                .build());
        HttpHeaders headers = new HttpHeaders();

        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                .model(model)
                .messages(messages)
                .maxTokens(maxToken)
                .temperature(temperature)
                .topP(topP)
                .build();
        headers.setContentType(MediaType.parseMediaType(mediaType));
        headers.add(authorization, bearer + apiKey);

        HttpEntity<ChatGPTRequest> httpEntity = new HttpEntity<>(chatGPTRequest, headers);
        ResponseEntity<ChatGPTResponse> responseEntity = restTemplate.postForEntity(
                url,
                httpEntity,
                ChatGPTResponse.class);

        return responseEntity.getBody();
    }
}