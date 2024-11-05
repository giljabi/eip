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
    public Results(String uuid, Long questionId, Integer answerNo,
                   Integer correctflag, String remoteip,
                   Integer qid) {
        this.uuid = uuid;
        this.questionId = questionId;
        this.answerNo = answerNo;
        this.correctflag = correctflag;
        this.remoteip = remoteip;
        this.qid = qid;
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

    private String remoteip;

    private Integer qid;

    public Results() {

    }

    @PrePersist
    protected void onCreate() {
        this.createat = LocalDateTime.now();
    }

}

