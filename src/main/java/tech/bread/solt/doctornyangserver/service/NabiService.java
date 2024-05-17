package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.CreateChatRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetChatResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface NabiService {

    String createChat(CreateChatRequest request, String id);
    List<GetChatResponse> getChats(String id);
    List<GetChatResponse> getChats(String id, int page);

    int feed(String id);
}
