package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;
import tech.bread.solt.doctornyangserver.model.entity.BMIRange;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.BMIRangeRepo;
import tech.bread.solt.doctornyangserver.repository.ScheduleRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.Gender;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final BMIRangeRepo bmiRangeRepo;
    private final ScheduleRepo scheduleRepo;
    @Override
    public int register(RegisterRequest request) {
        int result;
        Gender g;

        if (!isUnique(request.getId())) {
            System.out.println("회원가입 실패: 아이디 중복");
            result = 100;
        }
        else {
            String salt = getSalt();

            double bmi = calcBMI(request.getWeight(), request.getHeight());
            int bmiId = setBMIRangeId(bmi);
            BMIRange bmiRange = bmiRangeRepo.findOneById(bmiId);
            if (request.getHeight() > 251 || request.getHeight() < 65){
                System.out.println("키를 잘못 입력했습니다.");
                return 300;
            }
            if (request.getWeight() > 769 || request.getWeight() < 6 ) {
                System.out.println("체중을 잘못 입력했습니다.");
                return 300;
            }
            double bmr = calcBMR(request.getSex(), request.getWeight(), request.getHeight(), request.getAge());

            if (request.getSex().equals("남성"))
                g = Gender.MALE;
            else
                g = Gender.FEMALE;

            try {
                userRepo.save(User.builder()
                        .id(request.getId())
                        .password(hashing(request.getPassword(), salt))
                        .salt(salt)
                        .nickname(request.getNickname())
                        .birthDate(request.getBirthDate())
                        .gender(g)
                        .height(request.getHeight())
                        .weight(request.getWeight())
                        .bmr(bmr)
                        .bmiRangeId(bmiRange)
                        .doneTutorial(false)
                        .fed(false)
                        .likeability(0)
                        .userRole("User").build());
                System.out.println("회원가입 성공!");

                result = 200;
            }
            catch (NoSuchAlgorithmException e) {
                System.out.println("해싱 오류");
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public int login(LoginRequest request) {
        if (isUnique(request.getId())) {
            System.out.println("존재하지 않는 아이디");
            return 100;
        }
        else if(!checkPassword(request.getId(), request.getPassword())){
            System.out.println("비밀번호가 일치하지 않음");
            return 300;
        }
        else {
            Optional<User> u = userRepo.findById(request.getId());
            if (u.isPresent() && u.get().getDoneTutorial()){
                List<String> responses;
                responses = alertSchedule(u.get().getUid());
                System.out.println("유저 정보: " + u.get().getNickname() + "님");
                for (String s : responses)
                    System.out.println(s);
                return u.get().getUid();
            }
            else if(u.isPresent()){
                System.out.println("유저 정보: " + u.get().getNickname() + "님");

                //TODO 튜토리얼 진행
                System.out.println("튜토리얼을 진행합니다.");
                User user = u.get();
                user.setDoneTutorial(true);
                userRepo.save(user);

                return user.getUid();
            }
        }
        System.out.println("유저 정보를 찾을 수 없습니다.");
        return 400;
    }

    @Override
    public int enterBodyInformation(EnterBodyInformationRequest request) {
        double bmi = calcBMI(request.getWeight(), request.getHeight());
        int bmiId = setBMIRangeId(bmi);
        BMIRange bmiRange = bmiRangeRepo.findOneById(bmiId);
        Gender g;
        if (request.getHeight() > 251 || request.getHeight() < 65){
            System.out.println("키를 잘못 입력했습니다.");
            return 300;
        }
        if (request.getWeight() > 769 || request.getWeight() < 6 ) {
            System.out.println("체중을 잘못 입력했습니다.");
            return 300;
        }
        double bmr = calcBMR(request.getSex(), request.getWeight(), request.getHeight(), request.getAge());

        if (request.getSex().equals("남성"))
            g = Gender.MALE;
        else
            g = Gender.FEMALE;

        User user = userRepo.findOneByUid(request.getId());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setBirthDate(request.getBirth());
        user.setGender(g);
        user.setBmr(bmr);
        user.setBmiRangeId(bmiRange);
        userRepo.save(user);

        return 200;
    }

    @Override
    public UserInfoResponse showUser(int uid) {
        Optional<User> u = userRepo.findById(uid);
        UserInfoResponse userInfoResponse;
        String gender;
        if(u.isPresent()) {
            User user = u.get();
            Optional<BMIRange> bmi = bmiRangeRepo.findById(user.getBmiRangeId().getId());
            if (user.getGender().toString().equals("MALE"))
                gender = "남성";
            else gender = "여성";
            userInfoResponse = UserInfoResponse.builder()
                    .id(user.getId())
                    .nickName(user.getNickname())
                    .birth(user.getBirthDate())
                    .bmr(user.getBmr())
                    .gender(gender)
                    .weight(user.getWeight())
                    .height(user.getHeight())
                    .bmiRangeName(bmi.get().getName()).build();

            return userInfoResponse;
        }
        return null;
    }

    private boolean isUnique(String id){
        Optional<User> user = userRepo.findById(id);

        return user.isEmpty();
    }

    private boolean checkPassword(String id, String password){
        Optional<User> user = userRepo.findById(id);

        if(user.isPresent()){
            String salt = user.get().getSalt();

            try {
                if(hashing(password, salt).equals(user.get().getPassword())){
                    return true;
                }
            }
            catch (NoSuchAlgorithmException e){
                throw new RuntimeException(e);
            }
        }
        return false;
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
        return 0;
    }

    private double calcBMI(double weight, double height){
        double heightToMeter = height / 100;
        return weight / (heightToMeter * heightToMeter);
    }

    private String hashing(String password, String salt) throws NoSuchAlgorithmException {
        byte[] passwordBytes = password.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        for (int i = 0; i < 100; i++){
            String str = byteToString(passwordBytes) + salt;
            messageDigest.update(str.getBytes());
            passwordBytes = messageDigest.digest();
        }

        return byteToString(passwordBytes);
    }

    private String byteToString(byte[] bytes){
        StringBuilder sb = new StringBuilder();

        for(byte a : bytes){
            sb.append(String.format("%02x", a));
        }

        return sb.toString();
    }

    private String getSalt(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);

        return byteToString(bytes);
    }

    private List<String> alertSchedule(int userUid) {
        Optional<User> u = userRepo.findById(userUid);
        List<Schedule> scheduleOptional = scheduleRepo.findByUserUid(u.get());
        List<String> str = new ArrayList<>();

        for (Schedule s : scheduleOptional) {
            LocalDate today = LocalDate.now();
            if(today.isEqual(s.getDate().toLocalDate())){
                str.add(s.getText() + " 일정이 오늘입니다.");
            }
            else if (today.isBefore(s.getDate().toLocalDate())){
                str.add(s.getText() + " 일정이 "
                        + ChronoUnit.DAYS.between(today, s.getDate().toLocalDate())
                        + "일 남아 있습니다.");
            }
        }
        return str;
    }
}
