package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.ExamNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamNoRepository extends JpaRepository<ExamNo, Integer> {
    List<ExamNo> findAllByOrderByIdAsc();
}
