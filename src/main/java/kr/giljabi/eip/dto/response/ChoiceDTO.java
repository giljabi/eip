package kr.giljabi.eip.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChoiceDTO {
    private Long id;
    private Integer no;
    private String name;
    private String imageUrl;

    public ChoiceDTO() {
    }

    public ChoiceDTO(Long id, Integer no, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.no = no;
        this.imageUrl = imageUrl;
    }
}

