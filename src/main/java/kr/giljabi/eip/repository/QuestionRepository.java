package kr.giljabi.eip.repository;

import kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto;
import kr.giljabi.eip.model.ExamNo;
import kr.giljabi.eip.model.QName;
import kr.giljabi.eip.model.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    //전체문제를 확인할때 사용
    @Query(value = "SELECT * FROM question WHERE qid=:qid ORDER BY id", nativeQuery = true)
    List<Question> findRandomQuestionsTest(@Param("qid") Integer qid);

    // subjectId가 null일 때 사용할 쿼리
    //@Query(value = "SELECT * FROM (SELECT * FROM question WHERE id in(26, 30, 50) ORDER BY id) q ORDER BY RANDOM() LIMIT :limits", nativeQuery = true)
    @Query(value = "SELECT * FROM (SELECT * FROM question WHERE useflag=true and qid=:qid ORDER BY id) q ORDER BY RANDOM() LIMIT :limits", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("limits") int limits, @Param("qid") Integer qid);

    @Query(value = "SELECT * FROM (SELECT * FROM question WHERE subject_id = :subjectId AND useflag=true  and qid=:qid ORDER BY id) q ORDER BY RANDOM() LIMIT :limits", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("limits") int limits, Integer subjectId, @Param("qid") Integer qid);

    @Query("SELECT new kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto(q.correct, q.correctCount, q.replyCount) " +
            "FROM Question q WHERE q.id = :id AND q.qid = :qid")
    AnswerCorrectPercentageDto findCorrectByQuestionId(Long id, QName qid);

    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.replyCount = q.replyCount + 1 WHERE q.id = :id")
    void incrementReplyCount(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.correctCount = q.correctCount + 1 WHERE q.id = :id")
    void incrementCorrectCount(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Question q WHERE q.examNo = :examNo AND q.qid = :qname")
    int deleteAllByExamNoAndQid(@Param("examNo") ExamNo examNo, @Param("qname") QName qname);

    @Query("SELECT COALESCE(MAX(q.id), 0) FROM Question q")
    Long findMaxId();
}



