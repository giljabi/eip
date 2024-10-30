package kr.giljabi.eip.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerCorrectPercentageDto {
    private Integer correct;
    private Integer correctCount;
    private Integer replyCount;
    private Double correctPercentage;

    public AnswerCorrectPercentageDto(Integer correct, Integer correctCount, Integer replyCount) {
        this.correct = correct;
        this.correctCount = correctCount;
        this.replyCount = replyCount;
        if(correctCount == 0 || replyCount == 0)
            this.correctPercentage = 0.0;
        else {
            double percentage = (correctCount.doubleValue() / replyCount) * 100;
            this.correctPercentage = Math.round(percentage * 10) / 10.0;
        }
    }
}