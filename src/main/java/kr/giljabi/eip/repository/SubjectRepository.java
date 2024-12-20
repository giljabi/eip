package kr.giljabi.eip.repository;

import kr.giljabi.eip.dto.request.SubjectQuestionDTO;
import kr.giljabi.eip.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    //시험과목ID, 과목명
    List<Subject> findByQidOrderById(Integer qid);

    @Query(value = "SELECT b.id, b.name, a.cnt \n"+
            "FROM ( \n"+
            "    SELECT subject_id, COUNT(*) AS cnt \n"+
            "    FROM question \n"+
            "    WHERE qid = :qid \n"+
            "    GROUP BY subject_id \n"+
            ") a, subject b \n"+
            "WHERE a.subject_id = b.id \n"+
            "ORDER BY b.id", nativeQuery = true)
    List<SubjectQuestionDTO> findSubjectQuestions(@Param("qid") Integer qid);
}


