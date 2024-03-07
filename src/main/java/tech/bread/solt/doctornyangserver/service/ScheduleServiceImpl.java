package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.WeeklyCalendarRequest;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.ScheduleRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final UserRepo userRepo;

    @Override
    public int registerSchedule(ScheduleRegisterRequest request) {
        Optional<User> users = userRepo.findById(request.getUserUid());

        if (users.isPresent()) {
            scheduleRepo.save(Schedule.builder()
                    .userUid(users.get())
                    .text(request.getText())
                    .date(request.getDate()).build());

            System.out.println("스케줄 등록 성공 !");
            return 200;
        } else {
            System.out.println("스케줄 등록 실패");
            return 500;
        }
    }

    @Override
    public int deleteSchedule(int scheduleId) {
        Optional<Schedule> s = scheduleRepo.findById(scheduleId);

        if (s.isPresent()) {
            scheduleRepo.delete(s.get());
            System.out.println("일정 삭제 완료");
            return 200;
        }
        System.out.println("일정이 존재하지 않음");
        return 500;
    }

    @Override
    public Map<LocalDate, List<Schedule>> showWeeklySchedules(WeeklyCalendarRequest request) {
        Optional<User> users = userRepo.findById(request.getUserUid());
        Map<LocalDate, List<Schedule>> schedulesByLocalDate = new HashMap<>();
        List<Schedule> schedules;
        LocalDate today = LocalDate.now();
        int day = today.get(ChronoField.DAY_OF_WEEK);

        LocalDate startDate = today.minusDays(day);

        if (users.isPresent()) {
            for(int i = 0; i <= 6; i++) {
                LocalDate date = startDate.plusDays(i);
                schedules = scheduleRepo.findByUserUidAndDateBetween(users.get(),
                        date.atStartOfDay(), date.atTime(LocalTime.MAX));
                schedulesByLocalDate.put(date, schedules);
            }
        }
        return schedulesByLocalDate;
    }
}
