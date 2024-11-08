package kr.giljabi.eip.repository;

import kr.giljabi.eip.dto.query.ExamnoDayDTO;
import kr.giljabi.eip.model.ExamNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamNoRepository extends JpaRepository<ExamNo, Integer> {
    List<ExamNo> findAllByOrderByIdAsc();

    @Query(value =
            "SELECT b.id, b.examday "+
            "FROM ( "+
            "    SELECT examno_id "+
            "    FROM question "+
            "    WHERE qid = :qid "+
            "    GROUP BY examno_id "+
            ") AS a "+
            "LEFT JOIN examno b ON a.examno_id = b.id "+
            "ORDER BY b.id ", nativeQuery = true)
    List<Object[]> getExamnoDay(@Param("qid") Integer qid);
}

