package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterBodyInformationRequest {
    private int id;
    private String sex;
    private double height;
    private double weight;
    private int age;
}