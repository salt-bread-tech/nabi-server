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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NabiServiceImpl implements NabiService {

    final private UserRepo userRepo;
    final private ChatRepo chatRepo;

    private int CHAT_PAGE_SIZE = 20;
    private int LIKEABILITY_SCORE = 3;

    @Override
    public String createChat(CreateChatRequest request) {
        String result = "";
        Optional<User> optionalUser = userRepo.findById(request.getUid());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Chat> recentChats = chatRepo.findTop10ByUidOrderByCreateAtDesc(user);
            Collections.reverse(recentChats);
            int type = user.getLikeability() > 100 ? 1 : 2;

            GPTManager gptManager = new GPTManager(recentChats, type);
            ChatGPTResponse chatGPTResponse = gptManager.getResponse(request.getContent());
            result = chatGPTResponse.getChoices().get(0).getMessage().getContent();

            Chat userChat = Chat.builder()
                            .isUser(true)
                            .text(request.getContent())
                            .createAt(LocalDateTime.now())
                            .uid(optionalUser.get())
                            .build();

            Chat gptChat = Chat.builder()
                            .isUser(false)
                            .text(result)
                            .createAt(LocalDateTime.now())
                            .uid(optionalUser.get())
                            .build();

            chatRepo.save(userChat);
            chatRepo.save(gptChat);
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
            List<Chat> chats = chatRepo.findTop100ByUidOrderByCreateAtDesc(optionalUser.get());

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
            Pageable pageable = PageRequest.of(page, CHAT_PAGE_SIZE, Sort.by("createAt").descending());
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

    @Override
    public int feed(int uid) {
        int result = 400;
        Optional<User> optionalUser = userRepo.findById(uid);

        if (optionalUser.isEmpty()) {
            System.out.println("먹이 주기 실패: 유저가 존재하지 않음");
            return result;
        }
        else {
            User user = optionalUser.get();

            if (user.getFed()) {
                System.out.println("먹이 주기 실패: 오늘 먹이를 이미 줬습니다.");
                result = 300;
            }
            else {
                user.setFed(true);
                user.setLikeability(user.getLikeability() + LIKEABILITY_SCORE);
                System.out.println("먹이 주기 성공, 현재 호감도: " + user.getLikeability());
                userRepo.save(user);
                result = 200;
            }
        }

        return result;
    }


}
