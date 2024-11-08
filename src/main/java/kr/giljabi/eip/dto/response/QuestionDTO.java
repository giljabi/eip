package kr.giljabi.eip.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionDTO {
    private Long id = 0L;
    private Integer examNoId = 0;
    private String examNoName;
    private String examNoDay;
    private Integer subjectId = 0;
    private String subjectName;
    private Integer correct = 0;
    private String name;
    private String imageUrl;
    private Integer no = 0;
    private boolean questionImageFlag = false;
    private boolean choiceImageFlag = false;
    private boolean useFlag = false;
    private List<ChoiceDTO> choices;

    public QuestionDTO() {
    }

    public QuestionDTO(Long id,
                       Integer examNoId, String examNoName, String examNoDay, Integer subjectId, String subjectName,
                       Integer correct, String name, String imageUrl, Integer no,
                       boolean questionImageFlag, boolean choiceImageFlag,
                       boolean useFlag, List<ChoiceDTO> choices) {
        this.id = id;
        this.examNoId = examNoId;
        this.examNoName = examNoName;
        this.examNoDay = examNoDay;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.correct = correct;
        this.imageUrl = imageUrl;
        this.name = name;
        this.no = no;
        this.questionImageFlag = questionImageFlag;
        this.choiceImageFlag = choiceImageFlag;
        this.useFlag = useFlag;
        this.choices = choices;
    }

}


