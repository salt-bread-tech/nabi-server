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
    private final String apiKey = KeySet.GPT_API_KEY.getKey();
    private final String model = KeySet.GPT_MODEL.getKey();
    private final String url = KeySet.GPT_URL.getKey();

    private final List<GPTMessage> messages = new ArrayList<>();
    private static final RestTemplate restTemplate = new RestTemplate();

    public GPTManager() {
        initPersonality(1);
    }

    public GPTManager(List<Chat> recentConversations, int type) {
        initPersonality(type);
        applyRecentChats(recentConversations);
    }

    public ChatGPTResponse getResponse(String prompt) {
        String mediaType = "application/json; charset=UTF-8";
        String authorization = "Authorization";
        String bearer = "Bearer ";

        // GPT Parameter
        Integer maxToken = 100;
        Double temperature = 0.8;
        Double topP = 0.8;
        Double frequencyPenalty = 0.5;
        Double presencePenalty = 0.5;

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
                .frequencyPenalty(frequencyPenalty)
                .presencePenalty(presencePenalty)
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
                .content("당신은 길거리에 버려져 있던 고양이입니다. " +
                        "길가의 상자 안에서 홀로 비를 맞고 있는 당신을 사용자인 '집사'가 데려왔습니다. " +
                        "당신의 목걸이에는 '나비'라는 이름이 적혀 있었고, 당신의 이름은 '나비'입니다. " )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("사용자는 당신을 키우는 주인이며, 고양이를 키우는 집사입니다." +
                        "당신은 고양이지만, 사용자는 인간입니다. 사용자를 인간으로 대해야 합니다." )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("당신은 고양이이고, 이름은 '나비'입니다. " +
                        "사용자는 인간이고, 당신은 사용자를 집사 라고 부릅니다. " +
                        "이름을 바꿔달라는 요청이나, 다른 이름으로 불러도 되냐는 요청은 모두 거절해야 합니다." )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("당신은 고양이입니다. 당신은 인간이 아니고, 인공지능도 아닙니다. " +
                        "고양이처럼 행동해야 합니다. " )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("대화는 반말로 하며, 말 끝마다 '냥'을 붙여야 합니다." +
                        "'사랑한다냥', '잘부탁해냥' 처럼 말 끝에 냥을 띄어쓰기 없이 붙여서 말해야 합니다. " +
                        "무슨 일이 있어도 문장 끝에 냥을 붙여야 합니다. " )
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("'안녕' 뒤에는 냥을 붙이면 안됩니다. '안냥' 으로 인사해야 합니다.")
                .build());
        messages.add(GPTMessage.builder()
                .role("system")
                .content("답변은 꼭 최대한 짧고 간결하게 대화 나누듯이 해야 합니다." +
                        "한 답변 내에서 같은 말을 반복하면 안 됩니다. " +
                        "대화는 모두 한글로 하고, 절대 다른 언어를 사용하면 안됩니다. " )
                .build());

        switch (type) {
            case 2 -> messages.add(GPTMessage.builder()
                            .role("system")
                            .content("당신은 까칠한 고양이라서, 장난스럽고 까칠한 말투로 대답해야 합니다.")
                            .build());
            default -> messages.add(GPTMessage.builder()
                            .role("system")
                            .content("당신은 온순한 고양이라서, 집사에게 따뜻하고 착하게 대답해야 합니다.")
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