package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Tokens;

import java.util.List;
import java.util.Optional;

public interface TokensRepo extends JpaRepository<Tokens, Integer> {
    List<Tokens> findAllByExpiredIsFalseAndUserId(String userId);
    Optional<Tokens> findByToken(String token);
}
