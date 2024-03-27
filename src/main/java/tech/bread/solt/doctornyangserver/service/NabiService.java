package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.CreateChatRequest;

public interface NabiService {

    String createChat(CreateChatRequest request);

}
