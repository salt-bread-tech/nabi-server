package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.BMIRange;
import tech.bread.solt.doctornyangserver.model.entity.WeightManagementTip;

import java.util.List;
import java.util.Optional;

public interface WeightManagementTipRepo extends JpaRepository<WeightManagementTip, Integer> {
    List<WeightManagementTip> findByBmiRangeId(BMIRange bmiRangeId);
}
