package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    // 필요에 따라 추가적인 메서드를 정의할 수 있습니다.
}