package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.TokenUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenUsageRepository extends JpaRepository<TokenUsage, Long> {
    Optional<TokenUsage> findByDate(String date);
}