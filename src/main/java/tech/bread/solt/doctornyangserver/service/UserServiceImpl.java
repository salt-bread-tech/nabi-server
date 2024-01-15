package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    @Override
    public int register(RegisterRequest request) {
        int result;

        if (!isUnique(request.getId())) {
            System.out.println("회원가입 실패: 아이디 중복");
            result = 400;
        } else {
            userRepo.save(User.builder()
                    .id(request.getId())
                    .password(request.getPassword())
                    .nickname(request.getNickname())
                    .birthDate(request.getBirthDate())
                    .build());
            System.out.println("회원가입 성공!");
            result = 200;
        }
        return result;
    }

    @Override
    public int login(LoginRequest request) {
        if (isUnique(request.getId())) {
            System.out.println("존재하지 않는 아이디");
            return 400;
        }
        else if(!checkPassword(request.getId(), request.getPassword())){
            System.out.println("비밀번호가 일치하지 않음");
            return 500;
        }
        else {
            System.out.println("로그인 성공");
            return 200;
        }
    }

    private boolean isUnique(String id){
        Optional<User> user = userRepo.findById(id);

        return user.isEmpty();
    }

    private boolean checkPassword(String id, String password){
        Optional<User> user = userRepo.findById(id);

        return user.isPresent() && user.get().getPassword().equals(password);
    }
}
