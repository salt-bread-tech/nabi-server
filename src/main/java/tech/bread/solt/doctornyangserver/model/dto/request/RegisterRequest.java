package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String id;
    private String password;
    private String nickname;
//    private LocalDate birthDate;
//    private String sex;
//    private double height;
//    private double weight;
//    private int age;
}
