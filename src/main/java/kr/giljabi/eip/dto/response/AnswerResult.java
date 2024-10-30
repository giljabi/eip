package kr.giljabi.eip.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnswerResult {
    private Long questionId;
    private Integer userAnswer;
    private Integer correctAnswer;
    private boolean isCorrect;
    private Double correctPercentage;

    // 생성자
    public AnswerResult(Long questionId, Integer userAnswer,
                        Integer correctAnswer, boolean isCorrect,
                        Double correctPercentage) {
        this.questionId = questionId;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
        this.correctPercentage = correctPercentage;
    }

}
