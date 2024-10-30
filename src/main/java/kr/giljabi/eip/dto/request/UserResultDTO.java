package kr.giljabi.eip.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResultDTO {
    private String subjectName;
    private Long total;
    private Long correct;
    private Double correctPercentage;

    public UserResultDTO(String subjectName, Long total, Long correct) {
        this.subjectName = subjectName;
        this.total = total;
        this.correct = correct;
        if(correct == 0 || total == 0)
            this.correctPercentage = 0.0;
        else {
            double percentage = (correct.doubleValue() / total) * 100;
            this.correctPercentage = Math.round(percentage * 10) / 10.0;
        }
    }
}
