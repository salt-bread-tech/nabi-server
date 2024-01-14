package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@Builder
public class User {
    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer uid;

    @Column(name = "id")
    String id;

    @Column(name = "password")
    String password;

    @Column(name = "nickname")
    String nickname;

    @Column(name = "birth_date")
    Date birthDate;

    @Column(name = "height")
    Double height;

    @Column(name = "weight")
    Double weight;

    @Column(name = "bmr")
    Double bmr;

    @ManyToOne
    @JoinColumn(name = "bmi_range_id")
    BMIRange bmiRangeId;
}
