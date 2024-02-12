package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.ScheduleRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.util.Optional;

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
}
