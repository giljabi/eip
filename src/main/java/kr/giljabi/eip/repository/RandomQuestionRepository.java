package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.RandomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RandomQuestionRepository extends JpaRepository<RandomQuestion, Long> {
}
