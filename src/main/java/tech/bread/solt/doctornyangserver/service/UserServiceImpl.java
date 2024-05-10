package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.*;
import tech.bread.solt.doctornyangserver.model.entity.BMIRange;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.BMIRangeRepo;
import tech.bread.solt.doctornyangserver.repository.ScheduleRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.security.JwtProvider;
import tech.bread.solt.doctornyangserver.util.Gender;

import java.util.*;
import java.util.regex.Pattern;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final BMIRangeRepo bmiRangeRepo;
    private final ScheduleRepo scheduleRepo;

    private final JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public CountingDaysResponse countingDays(String userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()){
            User u = user.get();
            return CountingDaysResponse.builder()
                    .days((int)ChronoUnit.DAYS.between(u.getCreateAt(), LocalDate.now()) + 1).build();
        }
        return CountingDaysResponse.builder().build();
    }

    @Override
    public ResponseEntity<? super RegisterResponse> register(RegisterRequest request) {
        Pattern patternId = Pattern.compile("^[A-Za-z0-9]{4,}$");
        Pattern patternEmail = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(?:\\.[A-Za-z]{2,})?$");
        Pattern patternPassword = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*()\\-_=+\\\\\\|{}\\[\\]:;\"'<>,.?/]{8,20}$");
        Gender g;
        String userId = request.getId();
        boolean isExistId = userRepo.existsById(userId);

        if (isExistId) {
            return RegisterResponse.duplicateId();
        }
        else {
            if ((patternId.matcher(request.getId()).find() || patternEmail.matcher(request.getId()).find())
                    && patternPassword.matcher(request.getPassword()).find()) {

                double bmi = calcBMI(request.getWeight(), request.getHeight());
                int bmiId = setBMIRangeId(bmi);
                BMIRange bmiRange = bmiRangeRepo.findOneById(bmiId);
                if (request.getHeight() > 251 || request.getHeight() < 65){
                    System.out.println("키를 잘못 입력했습니다.");
                    return RegisterResponse.certificationFail();
                }
                if (request.getWeight() > 769 || request.getWeight() < 6 ) {
                    System.out.println("체중을 잘못 입력했습니다.");
                    return RegisterResponse.certificationFail();
                }
                double bmr = calcBMR(request.getSex(), request.getWeight(), request.getHeight(), request.getAge());

                if (request.getSex().equals("남성"))
                    g = Gender.MALE;
                else
                    g = Gender.FEMALE;

                try {
                    String password = request.getPassword();
                    String encodedPassword = passwordEncoder.encode(password);
                    request.setPassword(encodedPassword);
                    userRepo.save(User.builder()
                            .id(request.getId())
                            .password(request.getPassword())
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
                            .userRole("ROLE_USER")
                            .createAt(LocalDate.now()).build());
                    System.out.println("회원가입 성공!");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            else {
                return RegisterResponse.certificationFail();
            }
        }
        return RegisterResponse.success();
    }

    @Override
    public ResponseEntity<? super LoginResponse> login(LoginRequest request) {
        String token = null;
        int userUid = 0;
        try {

            String userId = request.getId();
            Optional<User> user = userRepo.findById(userId);
            if (user.isEmpty()) return LoginResponse.loginFail();

            String password = request.getPassword();
            String encodedPassword = user.get().getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            userUid = user.get().getUid();
            if (!isMatched) return LoginResponse.loginFail();

            // 토큰 생성
            token = jwtProvider.create(userId);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return LoginResponse.success(token, userUid);
    }

    @Override
    public int enterBodyInformation(EnterBodyInformationRequest request) {
        User u;
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

        Optional<User> user = userRepo.findById(request.getId());
        if(user.isPresent()) {
            u = user.get();
            u.setHeight(request.getHeight());
            u.setWeight(request.getWeight());
            u.setBirthDate(request.getBirth());
            u.setGender(g);
            u.setBmr(bmr);
            u.setBmiRangeId(bmiRange);
            userRepo.save(u);
        }
        return 200;
    }

    @Override
    public UserInfoResponse showUser(String id) {
        Optional<User> u = userRepo.findById(id);
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
