package kr.giljabi.eip.service;

import kr.giljabi.eip.model.RandomQuestion;
import kr.giljabi.eip.repository.RandomQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RandomQuestionService {

    private final RandomQuestionRepository randomQuestionRepository;

    @Autowired
    public RandomQuestionService(RandomQuestionRepository randomQuestionRepository) {
        this.randomQuestionRepository = randomQuestionRepository;
    }

    public RandomQuestion saveRandomQuestion(String uuid, Long questionId) {
        RandomQuestion randomQuestion = new RandomQuestion();
        randomQuestion.setUuid(uuid);
        randomQuestion.setQuestionId(questionId);
        return randomQuestionRepository.save(randomQuestion);
    }
}
