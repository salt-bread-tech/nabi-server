package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.WeeklyCalendarRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ScheduleListResponse;
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
    public int register(ScheduleRegisterRequest request) {
        Optional<User> users = userRepo.findById(request.getId());

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
    public boolean delete(int scheduleId) {
        Optional<Schedule> s = scheduleRepo.findById(scheduleId);
        if (s.isEmpty()) return false;
        try {
            scheduleRepo.deleteById(scheduleId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<LocalDate, List<ScheduleListResponse>> getScheduleList(LocalDate date, String id) {
        Optional<User> users = userRepo.findById(id);
        Map<LocalDate, List<ScheduleListResponse>> schedulesByLocalDate = new HashMap<>();

        int day = date.get(ChronoField.DAY_OF_WEEK);
        if (day == 7)
            day = 0;
        LocalDate startDate = date.minusDays(day);

        if (users.isPresent()) {
            for(int i = 0; i <= 6; i++) {
                List<ScheduleListResponse> schedules = new ArrayList<>();
                date = startDate.plusDays(i);
                List<Schedule> s = scheduleRepo.findByUserUidAndDateBetween(users.get(),
                        date.atStartOfDay(), date.atTime(LocalTime.MAX));
                for (Schedule schedule : s) {
                    ScheduleListResponse r = ScheduleListResponse.builder()
                            .scheduleId(schedule.getId())
                            .text(schedule.getText())
                            .date(schedule.getDate()).build();
                    schedules.add(r);
                }
                schedulesByLocalDate.put(date, schedules);
            }
        }
        return schedulesByLocalDate;
    }
}
