package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.ModifyUserRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.*;
import tech.bread.solt.doctornyangserver.model.entity.*;
import tech.bread.solt.doctornyangserver.repository.*;
import tech.bread.solt.doctornyangserver.security.JwtProvider;
import tech.bread.solt.doctornyangserver.util.Gender;

import java.util.*;
import java.util.regex.Pattern;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final BMIRangeRepo bmiRangeRepo;
    private final ScheduleRepo scheduleRepo;
    private final TokensRepo tokensRepo;
    private final WidgetRepo widgetRepo;

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

        if (request.getNickname().length() < 2 || request.getNickname().length() > 20) {
            log.warn("닉네임 길이 오류: {}", request.getNickname());
            return RegisterResponse.nicknameFormatError();
        }

        if (!patternId.matcher(request.getId()).find() && !patternEmail.matcher(request.getId()).find()) {
            log.warn("아이디 형식 오류: {}", request.getId());
            return RegisterResponse.idFormatError();
        }

        if (!patternPassword.matcher(request.getPassword()).find()) {
            log.warn("비밀번호 형식 오류: {}", request.getPassword());
            return RegisterResponse.passwordFormatError();
        }

        if (userRepo.existsById(request.getId())) {
            log.warn("아이디 중복: {}", request.getId());
            return RegisterResponse.duplicateId();
        }

        if (request.getHeight() > 251 || request.getHeight() < 65){
            log.warn("신장 입력 오류: {}", request.getHeight());
            return RegisterResponse.heightFormatError();
        }

        if (request.getWeight() > 769 || request.getWeight() < 6 ) {
            log.warn("체중 입력 오류: {}", request.getWeight());
            return RegisterResponse.weightFormatError();
        }

        Gender gender = request.getSex().equals("남성")? Gender.MALE : Gender.FEMALE;

        double bmr = calcBMR(request.getSex(), request.getWeight(), request.getHeight(), request.getAge());

        double bmi = calcBMI(request.getWeight(), request.getHeight());
        BMIRange bmiRange = bmiRangeRepo.findOneById(getBMIRangeId(bmi));

        try {
            User user = User.builder()
                    .id(request.getId())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .birthDate(request.getBirthDate())
                    .gender(gender)
                    .height(request.getHeight())
                    .weight(request.getWeight())
                    .bmr(bmr)
                    .bmiRangeId(bmiRange)
                    .doneTutorial(false)
                    .fed(false)
                    .likeability(0)
                    .userRole("ROLE_USER")
                    .createAt(LocalDate.now()).build();

            userRepo.save(user);
            log.info("회원가입 성공");

            widgetRepo.save(Widget.builder()
                    .userUid(user)
                    .used("01234").build());
            log.info("위젯 순서 등록 성공");
        }
        catch (Exception e) {
            log.error("Critical Exception {}", e.toString());
            throw new RuntimeException(e);
        }

        return RegisterResponse.success();
    }

    @Override
    public ResponseEntity<? super LoginResponse> login(LoginRequest request) {
        String token = null;
        int userUid = 0;
        boolean doneTutorial;
        try {

            String userId = request.getId();
            Optional<User> user = userRepo.findById(userId);
            if (user.isEmpty()) return LoginResponse.loginFail();

            String password = request.getPassword();
            String encodedPassword = user.get().getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            userUid = user.get().getUid();
            doneTutorial = user.get().getDoneTutorial();
            if (!doneTutorial) {
                toggleDoneTutorial(userId);
            }
            if (!isMatched) {
                log.warn("잘못된 비밀번호");
                return LoginResponse.loginFail();
            }
            // 토큰 생성
            token = jwtProvider.create(userId);

            updateTokens(userId);
            saveToken(userId, token);
        } catch (Exception e) {
            log.error("Database Error");
            return ResponseDto.databaseError();
        }
        return LoginResponse.success(token, userUid, doneTutorial);
    }

    @Override
    public int modifyUser(ModifyUserRequest request) {
        User u;
        double bmi = calcBMI(request.getWeight(), request.getHeight());
        int bmiId = getBMIRangeId(bmi);
        BMIRange bmiRange = bmiRangeRepo.findOneById(bmiId);
        Gender g;
        if (request.getHeight() > 251 || request.getHeight() < 65){
            log.warn("입력한 키가 유효 범위를 벗어남: {}", request.getHeight());
            return 300;
        }
        if (request.getWeight() > 769 || request.getWeight() < 6 ) {
            log.warn("입력한 몸무게가 유효 범위를 벗어남: {}", request.getWeight());
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
            log.info("사용자 정보 수정 완료 ID: {}", user.get().getId());
        }
        return 200;
    }

    @Override
    public UserInfoResponse getUser(String id) {
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
        log.warn("존재하지 않는 User Information {}", id);
        return null;
    }

    @Override
    public boolean deleteUser(String userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) return false;
        try {
            Optional<Widget> widget = widgetRepo.findByUserUid(user.get());
            widget.ifPresent(widgetRepo::delete);

            userRepo.deleteById(user.get().getUid());
            return true;
        } catch (Exception e) {
            log.error("Database Error");
            return false;
        }
    }

    private int getBMIRangeId(double bmi){
        List<BMIRange> ranges = bmiRangeRepo.findAllByOrderByIdAsc();

        if (bmi < ranges.get(0).getMax())
            return 0;

        for (int i = 1; i < ranges.size(); i++) {
            if (bmi < ranges.get(i).getMax()) {
                return i;
            }
        }

        return ranges.size()-1;
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

    private void saveToken(String userId, String token) {
        tokensRepo.save(Tokens.builder()
                .token(token)
                .userId(userId).build());
    }

    private void updateTokens(String userId){
        Optional<Tokens> t = tokensRepo.findByUserId(userId);

        t.ifPresent(tokensRepo::delete);
    }

    private void toggleDoneTutorial(String userId) {
        Optional<User> u = userRepo.findById(userId);
        User saveUser = u.get();
        saveUser.setDoneTutorial(true);
        userRepo.save(saveUser);
    }
}
