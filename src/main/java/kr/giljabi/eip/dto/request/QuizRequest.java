package kr.giljabi.eip.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class QuizRequest {
    private Long id;    //신규등록이면 null
    private boolean useFlag;
    //private boolean questionImageFlag;
    private boolean choiceImageFlag;
    private Integer examId;
    private Integer qid;
    private int subjectId;
    private int no;
    private String name;
    private int correct;
    private List<String> choices;

    // Getters and Setters
}


