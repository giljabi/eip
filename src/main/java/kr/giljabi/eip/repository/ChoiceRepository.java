package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.Choice;
import kr.giljabi.eip.model.ExamNo;
import kr.giljabi.eip.model.QName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Integer> {

    @Modifying
    @Query("DELETE FROM Choice c WHERE c.question.id IN (SELECT q.id FROM Question q WHERE q.examNo.id = :examNoId AND q.qid.id = :qid)")
    int deleteByExamNoIdAndQid(@Param("examNoId") Integer examNoId, @Param("qid") Integer qid);

}


