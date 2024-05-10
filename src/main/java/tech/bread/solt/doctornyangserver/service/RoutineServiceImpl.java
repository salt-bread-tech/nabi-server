package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.DeleteRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowRoutineResponse;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.RoutineRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

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
            routineRepo.save(Routine.builder()
                    .userUid(u.get())
                    .routineName(request.getName())
                    .maxPerform(request.getMaxPerform())
                    .startDate(request.getDate())
                    .colorCode(request.getColorCode())
                    .performCounts(0).build());
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
    public List<ShowRoutineResponse> show(String id) {
        Optional<User> u = userRepo.findById(id);
        List<ShowRoutineResponse> responses = new ArrayList<>();
        ShowRoutineResponse response;

        if (u.isPresent()) {
            List<Routine> routines = routineRepo.findByUserUid(u.get());

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
    public int delete(DeleteRoutineRequest request) {
        Optional<User> u = userRepo.findById(request.getUid());
        Optional<Routine> r = routineRepo.findById(request.getRid());
        if (u.isPresent()) {
            if (r.isPresent()) {
                routineRepo.delete(r.get());
                System.out.println("루틴 삭제 성공");
                return 200;
            }
            System.out.println("루틴 정보를 찾을 수 없음");
            return 400;
        }
        System.out.println("사용자 정보를 찾을 수 없음");
        return 500;
    }
}
