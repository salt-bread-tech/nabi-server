package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.IncrementRoutinePerformRequest;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.RoutineRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepo routineRepo;
    private final UserRepo userRepo;
    @Override
    public int register(RegisterRoutineRequest request) {
        Optional<User> u = userRepo.findById(request.getUid());
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
    public int increment(IncrementRoutinePerformRequest request) {
        Optional<User> u = userRepo.findById(request.getUid());
        if(u.isPresent()) {
            Optional<Routine> r = routineRepo.findById(request.getRid());
            if(r.isPresent()) {
                Routine routine = r.get();
                int performCountNow = routine.getPerformCounts();
                routine.setRoutineId(performCountNow + 1);
                routineRepo.save(routine);

                System.out.println("루틴 1회 성공 !");
                return 200;
            }
            System.out.println("루틴 정보를 찾을 수 없음");
            return 400;
        }
        System.out.println("사용자 정보를 찾을 수 없음");
        return 500;
    }

    }
}
