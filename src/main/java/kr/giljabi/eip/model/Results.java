package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@Table(name = "results")
public class Results {
    public Results(String uuid, Long questionId, Integer answerNo, Integer correctflag) {
        this.uuid = uuid;
        this.questionId = questionId;
        this.answerNo = answerNo;
        this.correctflag = correctflag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createat;

    @Column(length = 36)
    private String uuid;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "answer_no")
    private Integer answerNo;

    private Integer correctflag;

    public Results() {

    }

    @PrePersist
    protected void onCreate() {
        this.createat = LocalDateTime.now();
    }

}
