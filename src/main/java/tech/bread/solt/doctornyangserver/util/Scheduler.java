package tech.bread.solt.doctornyangserver.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.RoutineRepo;
import tech.bread.solt.doctornyangserver.repository.ScheduleRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final UserRepo userRepo;
    private final ScheduleRepo scheduleRepo;
    private final RoutineRepo routineRepo;

//    @Scheduled(cron = "*/5 * * * * *") // 테스트용
    @Scheduled(cron = "0 0 8 * * *")
    public void alertSchedule() {
        LocalDate today = LocalDate.now();
        List<User> users = userRepo.findAll();

        System.out.println("스케줄 시작");

        for (User u : users) {
            List<Schedule> s = scheduleRepo.findByUserUid(u);
            List<Schedule> todaySchedules = new ArrayList<>();

            for (Schedule schedule : s) {
                if (today.isEqual(schedule.getDate().toLocalDate()))
                    todaySchedules.add(schedule);
            }
            System.out.println(u.getNickname() + "님의 오늘 일정이 " + todaySchedules.size() + "개 있습니다.");
            int i = 1;
            for (Schedule schedule : todaySchedules){
                System.out.println(i + ". " + schedule.getText());
                i++;
            }
        }
    }

//    @Scheduled(cron = "*/5 * * * * *")
    @Scheduled(cron = "0 0 6 * * *")
    public void feedSchedule() {
        List<User> fedUsers = userRepo.findByFedIsTrue();
        List<User> users = new ArrayList<>();

        for (User user : fedUsers) {
            user.setFed(false);
            users.add(user);
        }

        userRepo.saveAll(users);
        System.out.println("먹이 주기 초기화 완료");
    }

    //@Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void renewRoutine() {
        List<Routine> allRoutines = routineRepo.findAll();
        List<Routine> topRoutines = new ArrayList<>();

        for (Routine routine : allRoutines) {
            topRoutines.add(routineRepo.findTopByUserUidAndRoutineNameOrderByTermDesc(
                    routine.getUserUid(),
                    routine.getRoutineName()));
        }
        for (Routine routine : topRoutines) {
            boolean isExist = routineRepo.existsByUserUidAndRoutineNameAndTerm(
                    routine.getUserUid(), routine.getRoutineName(), routine.getTerm() + 1);
            if (routine.getTerm() < routine.getMaxTerm() && !isExist) {
                Routine r = Routine.builder()
                        .userUid(routine.getUserUid())
                        .routineName(routine.getRoutineName())
                        .colorCode(routine.getColorCode())
                        .startDate(routine.getStartDate().plusDays(7))
                        .maxPerform(routine.getMaxPerform())
                        .performCounts(0)
                        .maxTerm(routine.getMaxTerm())
                        .term(routine.getTerm() + 1).build();
                routineRepo.save(r);
            }
        }
    }
}
