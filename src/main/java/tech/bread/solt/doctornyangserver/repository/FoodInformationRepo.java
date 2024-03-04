package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.FoodInformation;

import java.util.Optional;

public interface FoodInformationRepo extends JpaRepository<FoodInformation, Integer> {
    Optional<FoodInformation> findFoodInformationByNameAndServingSize(String name, double servingSize);
}
