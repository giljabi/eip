package kr.giljabi.eip.dto.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExamnoDayDTO {
    private Integer id;
    private String examday;

    public ExamnoDayDTO(Integer id, String examday) {
        this.id = id;
        this.examday = examday;
    }

}

