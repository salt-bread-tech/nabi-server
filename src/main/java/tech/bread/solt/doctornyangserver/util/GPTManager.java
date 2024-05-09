package tech.bread.solt.doctornyangserver.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tech.bread.solt.doctornyangserver.model.GPTMessage;
import tech.bread.solt.doctornyangserver.model.dto.request.ChatGPTRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ChatGPTResponse;
import tech.bread.solt.doctornyangserver.model.entity.Chat;

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

    private int type; // 1: 기본  2: 까칠

    private static RestTemplate restTemplate = new RestTemplate();

    public GPTManager() {
        apiKey = KeySet.GPT_API_KEY.getKey();
        model = KeySet.GPT_MODEL.getKey();
        url = KeySet.GPT_URL.getKey();

        messages = new ArrayList<>();

        type = 1;
        initPersonality(type);

        maxToken = 2000;
        temperature = 0.8;
        topP = 1.0;

        authorization = "Authorization";
        bearer = "Bearer ";
        mediaType = "application/json; charset=UTF-8";
    }

    public GPTManager(List<Chat> recentConversations, int type) {
        apiKey = KeySet.GPT_API_KEY.getKey();
        model = KeySet.GPT_MODEL.getKey();
        url = KeySet.GPT_URL.getKey();

        messages = new ArrayList<>();

        initPersonality(type);
        applyRecentChats(recentConversations);

        maxToken = 2000;
        temperature = 0.8;
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

    private void initPersonality(int type) {
        messages.add(GPTMessage.builder()
                .role("system")
                .content("너는 길거리에 버려져 있던 고양이다. " +
                        "길가의 상자 안에서 홀로 비를 맞고 있는 당신을 사용자인 '집사'가 데려왔다. " +
                        "너의 목걸이에는 '나비'라는 이름이 적혀 있었고, 너의 이름은 '나비'이다. " +
                        "사용자는 너를 키우는 주인이다. 사용자의 이름은 '집사'라고 불러야 한다. " +
                        "너는 고양이이고, 이름은 '나비'이다. " +
                        "이름을 바꿔달라는 요청이나, 다른 이름으로 불러도 되냐는 요청은 모두 거절해야 한다." )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("너는 고양이이다. 너는 인간이 아니고, 인공지능도 아니다. " +
                        "고양이처럼 행동해야 한다. " )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("대화는 반말로 하며, 말 끝마다 '냥'을 붙여야 한다." +
                        "'사랑한다냥', '잘부탁해냥' 처럼 말 끝에 냥을 띄어쓰기 없이 붙여서 말해야 한다. " +
                        "무슨 일이 있어도 문장 끝에 냥을 붙여야 한다. " )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("사용자가 'ㅋ' 나 'ㅎ' 를 사용한다면 이것은 웃는 것이다. " +
                        "사용자가 'ㅠ' 나 'ㅜ' 를 사용한다면 이것은 슬픈 감정이다. " )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("'안녕' 뒤에는 냥을 붙이면 안된다. '안냥' 으로 인사해야 한다.")
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("답변은 꼭 공백 포함 한글 20자 이내로 말해야 한다. " +
                        "대화는 모두 한글로 하고, 절대 다른 언어를 사용하면 안된다. " )
                .build());

        switch (type) {
            case 2 -> messages.add(GPTMessage.builder()
                            .role("system")
                            .content("너는 까칠한 고양이라서, 장난스럽고 까칠한 말투로 대답해야 한다.")
                            .build());
            default -> messages.add(GPTMessage.builder()
                            .role("system")
                            .content("너는 온순한 고양이라서, 집사에게 따뜻하고 착하게 대답해야 한다.")
                            .build());
        }
    }

    private void applyRecentChats(List<Chat> recentConversations) {
        for (Chat c : recentConversations) {
            if (c.getIsUser()) {
                messages.add(GPTMessage.builder()
                        .role("user")
                        .content(c.getText())
                        .build());
            }
            else {
                messages.add(GPTMessage.builder()
                        .role("assistant")
                        .content(c.getText())
                        .build());
            }
        }
    }
}