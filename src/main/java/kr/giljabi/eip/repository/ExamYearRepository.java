package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.ExamYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamYearRepository extends JpaRepository<ExamYear, Integer> {
    // 필요한 커스텀 쿼리 메서드를 여기에 추가할 수 있습니다.
}


