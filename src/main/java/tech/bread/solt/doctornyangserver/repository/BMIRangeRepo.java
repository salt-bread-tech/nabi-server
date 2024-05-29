package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.BMIRange;

import java.util.List;

public interface BMIRangeRepo extends JpaRepository<BMIRange, Integer> {
    BMIRange findOneById(int bmiId);
    BMIRange findById(int bmiId);

    List<BMIRange> findAllByOrderByIdAsc();
}
