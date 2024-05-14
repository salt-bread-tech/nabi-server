package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ScheduleListResponse;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.ScheduleRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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

        LocalDate startDate = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = date.with(TemporalAdjusters.lastDayOfMonth());

        if (users.isPresent()) {
            while (!startDate.isAfter(endDate)) {
                List<ScheduleListResponse> scheduleRes = new ArrayList<>();
                List<Schedule> schedules = scheduleRepo.findByUserUidAndDateBetween(users.get(),
                        startDate.atStartOfDay(), startDate.atTime(23, 59, 0));
                for (Schedule s : schedules) {
                    ScheduleListResponse r = ScheduleListResponse.builder()
                            .scheduleId(s.getId())
                            .text(s.getText())
                            .date(s.getDate()).build();
                    scheduleRes.add(r);
                }
                schedulesByLocalDate.put(startDate, scheduleRes);
                startDate = startDate.plusDays(1);
            }
        }
        return schedulesByLocalDate;
    }
}
