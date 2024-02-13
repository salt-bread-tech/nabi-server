package tech.bread.solt.doctornyangserver.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.ScheduleRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.service.ScheduleService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final UserRepo userRepo;
    private final ScheduleRepo scheduleRepo;

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
}
