package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.entity.BMIRange;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.BMIRangeRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final BMIRangeRepo bmiRangeRepo;
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

    @Override
    public int enterBodyInformation(EnterBodyInformationRequest request) {
        double bmi = calcBMI(request.getWeight(), request.getHeight());
        int bmiId = setBMIRangeId(bmi);
        BMIRange bmiRange = bmiRangeRepo.findOneById(bmiId);

        double bmr = calcBMR(request.getSex(), request.getWeight(), request.getHeight(), request.getAge());

        if(bmi < 0 || bmi > 200){
            System.out.println("유효하지 않은 BMI 값");
            return 400;
        } else if (bmr == 0) {
            System.out.println("유효하지 않은 BMR 값");
            return 500;
        } else {
            User user = userRepo.findOneByUid(request.getId());
            user.setHeight(request.getHeight());
            user.setWeight(request.getWeight());
            user.setBmr(bmr);
            user.setBmiRangeId(bmiRange);
            userRepo.save(user);

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

    private int setBMIRangeId(double bmi){
        int bmiId = 0;
        List<BMIRange> ranges = bmiRangeRepo.findAll();

        for (BMIRange b : ranges){
            if(bmi < 18.5)
                return 0;
            else if (bmi > 35.0) {
                return 5;
            } else if(b.getMax() < bmi){
                bmiId = b.getId() + 1;
            }
        }

        return bmiId;
    }

    private double calcBMR(String sex, double weight, double height, int age) {
        // 남성: 66.5 + (13.75 X 체중 kg) + (5.003 X 키 cm) - (6.75 X 나이)
        // 여성: 655.1 + (9.563 X 체중 kg) + (1.850 X 키 cm) - (4.676 X 나이)
        if(sex.equals("남성")) {
            return 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
        }
        else if(sex.equals("여성"))
            return 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        else
            return 0;
    }

    private double calcBMI(double weight, double height){
        double heightToMeter = height / 100;
        return weight / (heightToMeter * heightToMeter);
    }
}
