package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetRoutineTop3ByDateResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowRoutineResponse;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.RoutineRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepo routineRepo;
    private final UserRepo userRepo;
    @Override
    public int register(RegisterRoutineRequest request) {
        Optional<User> u = userRepo.findById(request.getId());
        if (u.isPresent()){
            for (int i = 0; i < request.getMaxTerm(); i++) {
                routineRepo.save(Routine.builder()
                        .userUid(u.get())
                        .routineName(request.getName())
                        .maxPerform(request.getMaxPerform())
                        .startDate(request.getDate().plusDays(i * 7L))
                        .maxTerm(request.getMaxTerm())
                        .term(i + 1)
                        .colorCode(request.getColorCode())
                        .performCounts(0).build());
            }
            System.out.println("루틴 등록 성공!");
            return 200;
        }
        System.out.println("유저 정보를 찾을 수 없음");
        return 400;
    }

    @Override
    public int update(UpdateRoutineRequest request) {
        Optional<User> u = userRepo.findById(request.getId());
        if(u.isPresent()) {
            Optional<Routine> r = routineRepo.findById(request.getRid());
            if(r.isPresent()) {
                Routine routine = r.get();
                if (request.getCounts() > routine.getMaxPerform()) {
                    System.out.println("Count를 잘못 입력함");
                    return 300;
                }
                routine.setPerformCounts(request.getCounts());
                routineRepo.save(routine);
                System.out.println("루틴 정보 변경 성공");
                return 200;
            }
            System.out.println("루틴 정보를 찾을 수 없음");
            return 400;
        }
        System.out.println("사용자 정보를 찾을 수 없음");
        return 500;
    }

    @Override
    public List<ShowRoutineResponse> show(LocalDate date, String id) {
        Optional<User> u = userRepo.findById(id);
        List<ShowRoutineResponse> responses = new ArrayList<>();
        ShowRoutineResponse response;

        if (u.isPresent()) {
            int dayOfWeek = date.get(ChronoField.DAY_OF_WEEK);
            if (dayOfWeek == 7)
                dayOfWeek = 0;
            LocalDate startDate = date.minusDays(dayOfWeek);
            LocalDate endDate = startDate.plusDays(6);
            List<Routine> routines = routineRepo.findByUserUidAndStartDateBetween(u.get(), startDate, endDate);

            if (routines.isEmpty()) {
                System.out.println("등록된 루틴이 없습니다.");
                return null;
            }

            for (Routine r: routines) {
                response = ShowRoutineResponse.builder()
                        .id(r.getRoutineId())
                        .name(r.getRoutineName())
                        .color(r.getColorCode())
                        .max(r.getMaxPerform())
                        .counts(r.getPerformCounts()).build();
                responses.add(response);
            }

            System.out.println("루틴 정보 불러오기 성공");
            return responses;
        }
        return null;
    }

    @Override
    public boolean delete(int routineId) {
        Optional<Routine> r = routineRepo.findById(routineId);
        if (r.isEmpty())
            return false;
        try {
            routineRepo.deleteById(routineId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<GetRoutineTop3ByDateResponse> getRoutineTop3ByDate(LocalDate date, String id) {
        Optional<User> optionalUser = userRepo.findById(id);
        List<GetRoutineTop3ByDateResponse> responses = new ArrayList<>();

        if (optionalUser.isPresent()) {
            int dayOfWeek = date.get(ChronoField.DAY_OF_WEEK);
            if (dayOfWeek == 7)
                dayOfWeek = 0;
            LocalDate startDate = date.minusDays(dayOfWeek);
            LocalDate endDate = startDate.plusDays(6);

            List<Routine> routines = routineRepo.findTop3ByUserUidAndStartDateBetweenOrderByStartDateAscRoutineIdAsc(optionalUser.get(), startDate, endDate);

            if (routines.isEmpty()) {
                System.out.println("루틴 조회 실패: 등록된 루틴이 없음");
                return responses;
            }

            for (Routine r: routines) {
                GetRoutineTop3ByDateResponse response = GetRoutineTop3ByDateResponse.builder()
                        .id(r.getRoutineId())
                        .name(r.getRoutineName())
                        .color(r.getColorCode())
                        .max(r.getMaxPerform())
                        .counts(r.getPerformCounts()).build();
                responses.add(response);
            }

            System.out.println("루틴 조회 성공");
            return responses;
        }
        else {
            System.out.println("루틴 조회 실패: 유저가 존재하지 않음");
            return responses;
        }
    }
}
