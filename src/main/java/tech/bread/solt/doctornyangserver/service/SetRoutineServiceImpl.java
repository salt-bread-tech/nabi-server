package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.DeletePrivateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.PrivateRoutineRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.TogglePrivateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.SetRoutine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.RoutineRepo;
import tech.bread.solt.doctornyangserver.repository.SetRoutineRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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
                        .completion(false).build());
            }
        }
    }

    @Override
    public int deletePrivateRoutine(DeletePrivateRoutineRequest request) {
        Optional<User> user = userRepo.findById(request.getUserUid());
        Optional<Routine> optionalRoutine = routineRepo.findById(request.getRoutineId());
        
        if (user.isPresent() && optionalRoutine.isPresent()){
            List<SetRoutine> setRoutines = setRoutineRepo.findByRoutineIdAndUserUid(optionalRoutine.get(), user.get());

            if (setRoutines.isEmpty()){
                System.out.println("삭제하고자 하는 데일리 루틴이 존재하지 않음");
                return 400;
            }
            for (SetRoutine s : setRoutines)
                setRoutineRepo.delete(s);

            System.out.println("데일리 루틴 삭제 성공");
            return 200;
        }
        System.out.println("유저 아이디 혹은 루틴 정보가 존재하지 않음");
        return 500;
    }

    @Override
    public int togglePrivateRoutine(TogglePrivateRoutineRequest request) {
        Optional<Routine> optionalRoutine = routineRepo.findById(request.getRoutineId());
        Optional<User> optionalUser = userRepo.findById(request.getUserUid());

        List<SetRoutine> setRoutine = setRoutineRepo.findByUserUidAndRoutineIdAndPerformDateAndCompletionFalse(
                optionalUser.get(), optionalRoutine.get(), LocalDate.now()
        );

        if (setRoutine.isEmpty()){
            System.out.println("찾고자 하는 데일리루틴 정보가 없음");
            return 400;
        }
        SetRoutine setRoutineToToggle = setRoutine.get(0);
        setRoutineToToggle.setCompletion(true);
        setRoutineRepo.save(setRoutineToToggle);
        System.out.println("데일리 루틴 성공 !");
        return 200;
    }
}
