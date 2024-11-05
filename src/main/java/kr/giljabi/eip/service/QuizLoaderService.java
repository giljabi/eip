package kr.giljabi.eip.service;

import kr.giljabi.eip.model.*;
import kr.giljabi.eip.repository.AnswerRepository;
import kr.giljabi.eip.repository.ChoiceRepository;
import kr.giljabi.eip.repository.ExamNoRepository;
import kr.giljabi.eip.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class QuizLoaderService {

    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final AnswerRepository answerRepository;
    private final ExamNoRepository examNoRepository;

    @Autowired
    public QuizLoaderService(QuestionRepository questionRepository,
                             ChoiceRepository choiceRepository,
                             AnswerRepository answerRepository,
                             ExamNoRepository examNoRepository) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.answerRepository = answerRepository;
        this.examNoRepository = examNoRepository;
    }

    @Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public int  deleteAllByQuestionExamNoAndQid(ExamNo examNo, QName qname) {
        return questionRepository.deleteAllByExamNoAndQid(examNo, qname);
    }

    public int deleteAllChoiceExamNoIdAndQid(Integer examNoId, Integer qid) {
        return choiceRepository.deleteByExamNoIdAndQid(examNoId, qid);
    }

    public ExamNo findExamNoById(Integer id) {
        return examNoRepository.findById(id).orElse(null);
    }

    public Long findQuestionMaxId() {
        return questionRepository.findMaxId();
    }

    @Transactional
    public Choice saveChoice(Choice choice) {
        return choiceRepository.save(choice);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetQuestionIdSequence(Long start) {
        entityManager.createNativeQuery("ALTER SEQUENCE question_id_seq RESTART WITH " + start.longValue())
                .executeUpdate();
    }

}

