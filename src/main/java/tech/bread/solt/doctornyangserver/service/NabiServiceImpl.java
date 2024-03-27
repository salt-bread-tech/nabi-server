package tech.bread.solt.doctornyangserver.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.CreateChatRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ChatGPTResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetChatResponse;
import tech.bread.solt.doctornyangserver.model.entity.Chat;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.ChatRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.GPTManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NabiServiceImpl implements NabiService {

    final private UserRepo userRepo;
    private final ChatRepo chatRepo;

    @Override
    public String createChat(CreateChatRequest request) {
        String result = "";
        List<Chat> chats = new ArrayList<>();

        System.out.println(request.getUid());
        System.out.println(request.getQuestion());

        Optional<User> optionalUser = userRepo.findById(request.getUid());

        if (optionalUser.isPresent()) {
            ChatGPTResponse chatGPTResponse = GPTManager.getInstance().getResponse(request.getQuestion());
            result = chatGPTResponse.getChoices().get(0).getMessage().getContent();

            chats.add(Chat.builder()
                            .isUser(true)
                            .text(request.getQuestion())
                            .createAt(LocalDateTime.now())
                            .uid(optionalUser.get())
                            .build());

            chats.add(Chat.builder()
                            .isUser(false)
                            .text(result)
                            .createAt(LocalDateTime.now())
                            .uid(optionalUser.get())
                            .build());

            chatRepo.saveAll(chats);
        }
        else {
            result = "UID가 올바르지 않습니다.";
        }

        return result;
    }

    @Override
    public List<GetChatResponse> getChats(int uid) {
        List<GetChatResponse> result = new ArrayList<>();
        Optional<User> optionalUser = userRepo.findById(uid);

        if (optionalUser.isPresent()) {
            List<Chat> chats = chatRepo.findTop20ByUidOrderByCreateAtDesc(optionalUser.get());

            for (Chat c: chats) {
                System.out.println(c.getUid() + " " + c.getText());
                result.add(GetChatResponse.builder()
                                .isUser(c.getIsUser())
                                .content(c.getText())
                                .createAt(c.getCreateAt())
                                .build());
            }
        }

        return result;
    }

    @Override
    public List<GetChatResponse> getChats(int uid, int page) {
        List<GetChatResponse> result = new ArrayList<>();
        Optional<User> optionalUser = userRepo.findById(uid);

        if (optionalUser.isPresent()) {
            Pageable pageable = PageRequest.of(page, 20, Sort.by("createAt").descending());
            Page<Chat> chatPage = chatRepo.findByUidAndCreateAtBefore(optionalUser.get(), LocalDateTime.now(), pageable);
            List<Chat> chats = chatPage.getContent();

            for (Chat c: chats) {
                System.out.println(c.getUid() + " " + c.getText());
                result.add(GetChatResponse.builder()
                        .isUser(c.getIsUser())
                        .content(c.getText())
                        .createAt(c.getCreateAt())
                        .build());
            }
        }

        return result;
    }

}
