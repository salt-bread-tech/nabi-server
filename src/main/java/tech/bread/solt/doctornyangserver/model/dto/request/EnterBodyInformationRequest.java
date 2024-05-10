package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class EnterBodyInformationRequest {
    private String id;
    private String sex;
    private double height;
    private double weight;
    private int age;
    private LocalDate birth;
}
