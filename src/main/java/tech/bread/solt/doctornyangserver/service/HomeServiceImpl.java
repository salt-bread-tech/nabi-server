package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.response.HomeResponse;
import tech.bread.solt.doctornyangserver.model.entity.*;
import tech.bread.solt.doctornyangserver.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final UserRepo userRepo;
    private final ScheduleRepo scheduleRepo;
    private final IngestionRepo ingestionRepo;
    private final RoutineRepo routineRepo;
    private final PrescriptionRepo prescriptionRepo;
    private final DosageRepo dosageRepo;
    private final MedicineRepo medicineRepo;

    @Override
    public HomeResponse getHomeData(LocalDate date, String id) {
        HomeResponse homeResponse = new HomeResponse();
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isPresent()) {
            User user= optionalUser.get();

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            int dayOfWeek = date.get(ChronoField.DAY_OF_WEEK);
            if (dayOfWeek == 7)
                dayOfWeek = 0;

            LocalDate startDate = date.minusDays(dayOfWeek);
            LocalDate endDate = startDate.plusDays(6);

            List<Schedule> scheduleList = scheduleRepo.findByUserUidAndDateBetween(user, startOfDay, endOfDay);
            List<Ingestion> ingestionList = ingestionRepo.findAllByUserUidAndDate(user, date);
            List<Routine> routineList = routineRepo.findTop3ByUserUidAndStartDateBetweenOrderByStartDateAscRoutineIdAsc(user, startDate, endDate);
            List<Prescription> prescriptionList = prescriptionRepo.findTop3ByUserUidAndDateLessThanEqualOrderByDateDescIdDesc(user, date);
            List<Dosage> dosageList = dosageRepo.findByUserUidAndDate(user, date);

            List<HomeResponse.HomeSchedule> homeSchedules = new ArrayList<>();
            HomeResponse.HomeDiet homeDiet = homeResponse.new HomeDiet();
            List<HomeResponse.HomeRoutine> homeRoutines = new ArrayList<>();
            List<HomeResponse.HomePrescription> homePrescriptions = new ArrayList<>();
            List<String> homeDosages = new ArrayList<>();

            for (Schedule s : scheduleList) {
                homeSchedules.add(homeResponse.new HomeSchedule(s.getDate(), s.getText()));
            }

            double totalKcal = 0;
            double breakfastKcal = 0;
            double lunchKcal = 0;
            double dinnerKcal = 0;
            double snackKcal = 0;
            double totalCarbohydrate = 0;
            double totalProtein = 0;
            double totalFat = 0;

            for (Ingestion i : ingestionList) {
                FoodInformation food = i.getFoodId();

                switch (i.getIngestionTimes()) {
                    case BREAKFAST -> breakfastKcal += food.getCalories();
                    case LUNCH -> lunchKcal += food.getCalories();
                    case DINNER -> dinnerKcal += food.getCalories();
                    case SNACK -> snackKcal += food.getCalories();
                    default -> System.out.println("times error");
                }

                if (food.getCarbohydrate() != 9999999.0) {
                    totalCarbohydrate += food.getCarbohydrate();
                }

                if (food.getProtein() != 9999999.0) {
                    totalProtein += food.getProtein();
                }

                if (food.getFat() != 9999999.0) {
                    totalFat += food.getFat();
                }
            }

            totalKcal = breakfastKcal + lunchKcal + dinnerKcal + snackKcal;

            homeDiet.setTotalKcal(totalKcal);
            homeDiet.setBreakfastKcal(breakfastKcal);
            homeDiet.setLunchKcal(lunchKcal);
            homeDiet.setDinnerKcal(dinnerKcal);
            homeDiet.setSnackKcal(snackKcal);
            homeDiet.setTotalCarbohydrate(totalCarbohydrate);
            homeDiet.setTotalProtein(totalProtein);
            homeDiet.setTotalFat(totalFat);

            for (Routine r: routineList) {
                homeRoutines.add(homeResponse.new HomeRoutine(
                        r.getRoutineId(),
                        r.getRoutineName(),
                        r.getMaxPerform(),
                        r.getColorCode(),
                        r.getPerformCounts()));
            }

            for (Prescription p : prescriptionList) {
                homePrescriptions.add(homeResponse.new HomePrescription(p.getName(), p.getDate()));
            }

            for (Dosage d : dosageList) {
                System.out.println(d);
                Optional<Medicine> optionalMedicine = medicineRepo.findById(d.getMedicineId().getId());

                if (optionalMedicine.isPresent()) {
                    Medicine medicine = optionalMedicine.get();
                    System.out.println(medicine);

                    if (!homeDosages.contains(medicine.getMedicineName())) {
                        homeDosages.add(medicine.getMedicineName());
                    }
                }
            }

            homeResponse.setSchedules(homeSchedules);
            homeResponse.setDiets(homeDiet);
            homeResponse.setRoutines(homeRoutines);
            homeResponse.setPrescriptions(homePrescriptions);
            homeResponse.setDosages(homeDosages);
        }
        else {
            System.out.println("유저가 존재하지 않음");
            return homeResponse;
        }

        System.out.println(homeResponse);

        return homeResponse;
    }

}
