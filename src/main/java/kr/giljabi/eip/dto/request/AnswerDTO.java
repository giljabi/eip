package kr.giljabi.eip.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnswerDTO {
    private Long id;  // question_id
    private String answer;  // 선택된 choice.no
    private boolean correct;  // 정답 여부
    private Integer qid;
}
