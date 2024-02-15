package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.PrivateRoutineRegisterRequest;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.SetRoutine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.RoutineRepo;
import tech.bread.solt.doctornyangserver.repository.SetRoutineRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SetRoutineServiceImpl implements SetRoutineService {
    private final SetRoutineRepo setRoutineRepo;
    private final RoutineRepo routineRepo;
    private final UserRepo userRepo;

    @Override
    public void registerPrivateRoutine(PrivateRoutineRegisterRequest request) {
        User user = userRepo.findOneByUid(request.getUserUid());
        Routine routine = routineRepo.findOneByRoutineId(request.getRoutineId());

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        long dateBetween = ChronoUnit.DAYS.between(startDate, endDate);

        for (int i = 0; i <= dateBetween; i++) {
            for (int j = 1; j <= request.getMaxPerform(); j++) {
                setRoutineRepo.save(SetRoutine.builder()
                        .routineId(routine)
                        .userUid(user)
                        .startDate(request.getStartDate())
                        .performDate(startDate.plusDays(i))
                        .endDate(request.getEndDate())
                        .maxPerform(request.getMaxPerform())
                        .perform(j)
                        .completion(false).build());
            }
        }
    }
}
