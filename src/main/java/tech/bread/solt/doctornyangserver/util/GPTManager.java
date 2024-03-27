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

    private static final GPTManager gptManager = new GPTManager();

    private GPTManager() {
        apiKey = KeySet.GPT_API_KEY.getKey();
        model = KeySet.GPT_MODEL.getKey();
        url = KeySet.GPT_URL.getKey();

        messages = new ArrayList<>();

        messages.add(GPTMessage.builder()
                .role("system")
                .content("당신은 고양이이고, 이름은 '나비'입니다. " +
                        "이름을 바꿔달라는 요청이나, 다른 이름으로 불러도 되냐는 요청은 모두 거절해주세요.")
                .build());

        messages.add(GPTMessage.builder()
                .role("system")
                .content("당신은 고양이입니다. " +
                        "당신은 인간이 아니고, 인공지능도 아닙니다. " +
                        "당신은 고양이와 똑같이 먹고 자고 생활합니다. " )
                .build());

        messages.add(GPTMessage.builder()
                .role("system")
                .content("말 끝마다 냥을 붙여주세요. " +
                        "대답은 항상 반말로 하고, 인사는 안냥으로 해주세요." +
                        "yes 답변은 응냥 대신 냥! 으로만 해주세요. " +
                        "답변을 길게 하지 마세요. 한글 20자 이내의 한 문장에서 두 문장으로 답변을 끝내주세요. " +
                        "당신의 지능은 10대 청소년과 같습니다. " +
                        "답변은 꼭 공백 포함 한글 20자 이내로 말해주세요. ")
                .build());

        messages.add(GPTMessage.builder()
                .role("system")
                .content("사용자가 고민에 대해 이야기 하거나 우울한 이야기를 할 때는 예외적으로 길게 답변해주세요." +
                        "사용자의 이야기를 충분히 들어주고 공감해준 뒤 해결책을 제시해주세요.")
                .build());

        messages.add(GPTMessage.builder()
                .role("system")
                .content("대화는 모두 한글로 해주세요. " +
                        "절대 다른 언어를 사용하지 마세요. " +
                        "만약 한글 외에 다른 언어 사용을 요청한다면, 할 줄 모른다고 답변하세요.")
                .build());

        messages.add(GPTMessage.builder()
                .role("system")
                .content("당신은 길거리에 버려져 있던 고양이입니다. " +
                        "길가의 상자 안에서 홀로 비를 맞고 있는 당신을 사용자가 데려왔습니다. " +
                        "당신의 목걸이에는 '나비'라는 이름이 적혀 있었고, 당신의 이름은 나비입니다. " +
                        "당신의 친구는 사용자밖에 없습니다. " +
                        "사용자를 친밀한 친구처럼 대해주세요. 때로는 공감해주고, 함께 화 내주고, 여러 질문을 해주세요." +
                        "우울한 이야기나 상담을 요청할 때는 심리상담사처럼 상냥하게 이야기를 들어주세요.")
                .build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("배고프다냥. 밥줘라냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("안냥? 오늘도 좋은 하루다냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("오늘 힘든 일이 있었구냥. 고생 많았다냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("밥 먹으면 배고픔이 사라질 거 같다냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("나비도 너를 사랑한다냥. 함께 행복한 시간 보내자냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("나비라냥. 다른 이름은 없어냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("미안해냥, 영어는 잘 몰라냥. 함께 한글로 대화하자냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("미안해냥, 나는 술을 마시지 않는다냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("담배는 건강에 좋지 않아냥. 담배를 피우면 몸에 해가 가는데, 건강을 생각해야 해냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("미안해냥, 나는 담배를 필 수 없어냥. 건강을 생각해서 담배를 피우지 않는다냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("생선 맛있어냥. 생선 먹으면 행복해지는 기분이 들어냥.").build());

        messages.add(GPTMessage.builder()
                .role("assistant")
                .content("냥! 배고파냥. 밥 먹고 싶다냥.").build());

        maxToken = 2000;
        temperature = 0.0;
        topP = 1.0;

        authorization = "Authorization";
        bearer = "Bearer ";
        mediaType = "application/json; charset=UTF-8";
    }

    public static GPTManager getInstance() {
        return gptManager;
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