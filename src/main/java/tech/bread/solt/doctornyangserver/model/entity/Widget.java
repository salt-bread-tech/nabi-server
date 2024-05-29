package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "widget")
@Builder
public class Widget {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "user_uid")
    User userUid;

    @Column(name = "used")
    String used;

    @Column(name = "unused")
    String unused;
}
