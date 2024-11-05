package kr.giljabi.eip.service;

import kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto;
import kr.giljabi.eip.model.Question;
import kr.giljabi.eip.model.RandomQuestion;
import kr.giljabi.eip.repository.QuestionRepository;
import kr.giljabi.eip.repository.RandomQuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final RandomQuestionRepository randomQuestionRepository;

    @Value("${giljabi.question.max}")
    public Integer maxQuestion;

    public QuestionService(QuestionRepository questionRepository,
                           RandomQuestionRepository randomQuestionRepository) {
        this.questionRepository = questionRepository;
        this.randomQuestionRepository = randomQuestionRepository;
    }

    public List<Question> getRandomQuestions(Integer qId, Integer subjectId, String uuid, String remoteip) {
        List<Question> lists;
        if(subjectId == 0) {
            lists = questionRepository.findRandomQuestions(maxQuestion, qId);
            //lists = questionRepository.findRandomQuestionsTest(qId);
        } else
            lists = questionRepository.findRandomQuestions(maxQuestion, subjectId, qId);

        // 가져온 List<Question>을 List<RandomQuestion>으로 변환
        List<RandomQuestion> randomQuestions = lists.stream()
                .map(question -> {
                    RandomQuestion randomQuestion = new RandomQuestion();
                    randomQuestion.setUuid(uuid);
                    randomQuestion.setQuestionId(question.getId());
                    randomQuestion.setRemoteip(remoteip);
                    return randomQuestion;
                })
                .collect(Collectors.toList());

        // 변환된 List<RandomQuestion>을 randomQuestionRepository에 저장
        randomQuestionRepository.saveAll(randomQuestions);

        return lists;
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

