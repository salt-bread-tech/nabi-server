package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    String id;
    String nickName;
    LocalDate birth;
    double height;
    double weight;
    double bmr;
    String bmiRangeName;
}
