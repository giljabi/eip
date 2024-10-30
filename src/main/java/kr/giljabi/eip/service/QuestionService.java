package kr.giljabi.eip.service;

import kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto;
import kr.giljabi.eip.model.Question;
import kr.giljabi.eip.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Value("${giljabi.question.max}")
    public Integer maxQuestion;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getRandomQuestions(Integer subjectId) {
        if(subjectId == 0)
            return questionRepository.findRandomQuestions(maxQuestion);
        else
            return questionRepository.findRandomQuestions(maxQuestion, subjectId);
    }

    public AnswerCorrectPercentageDto findCorrectByQuestionId(Long questionId) {
        return questionRepository.findCorrectByQuestionId(questionId);
    }

    public void incrementReplyCount(Long questionId) {
        questionRepository.incrementReplyCount(questionId);
    }

    public void incrementCorrectCount(Long questionId) {
        questionRepository.incrementCorrectCount(questionId);
    }
}