package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.AiQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiQueryRepository extends JpaRepository<AiQuery, Long> {
    boolean existsByQuestionIdAndAiModel(Long questionId, String aiModel);
    AiQuery findByQuestionId(Long questionId);
}
