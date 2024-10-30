package kr.giljabi.eip.service;

import kr.giljabi.eip.model.Answer;
import kr.giljabi.eip.model.Choice;
import kr.giljabi.eip.model.ExamNo;
import kr.giljabi.eip.model.Question;
import kr.giljabi.eip.repository.AnswerRepository;
import kr.giljabi.eip.repository.ChoiceRepository;
import kr.giljabi.eip.repository.ExamNoRepository;
import kr.giljabi.eip.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //@Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    //@Transactional
    public Choice saveChoice(Choice choice) {
        return choiceRepository.save(choice);
    }

    //@Transactional
    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }
    //@Transactional
    public ExamNo saveExamNo(ExamNo examNo) {
        return examNoRepository.save(examNo);
    }
}