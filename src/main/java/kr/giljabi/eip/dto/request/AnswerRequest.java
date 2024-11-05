package kr.giljabi.eip.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class AnswerRequest {
    private Integer subjectId;
    private ArrayList<AnswerDTO> answers;
    private String qid;
}
