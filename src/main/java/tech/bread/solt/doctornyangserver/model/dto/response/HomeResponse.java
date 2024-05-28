package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class HomeResponse {

    private List<HomeSchedule> schedules;
    private HomeDiet diets;
    private List<HomeRoutine> routines;
    private List<HomePrescription> prescriptions;
    private List<String> dosages;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public class HomeSchedule {
        private LocalDateTime dateTime;
        private String text;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public class HomeDiet {
        private double totalKcal;
        private double breakfastKcal;
        private double lunchKcal;
        private double dinnerKcal;
        private double snackKcal;
        private double totalCarbohydrate;
        private double totalProtein;
        private double totalFat;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public class HomeRoutine {
        int id;
        String name;
        int max;
        String color;
        int counts;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public class HomePrescription {
        private String name;
        private LocalDate date;
    }
}
