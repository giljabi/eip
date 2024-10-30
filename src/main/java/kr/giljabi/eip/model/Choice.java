package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "choice")
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
/*
    @ManyToOne
    @JoinColumn(name = "examno_id", nullable = false)
    private ExamNo examNo;

    @Column(name = "question_no", nullable = false)
    private int questionNo;*/

    @Column(nullable = false)
    private Integer no;

    @Column(nullable = false)
    private String name;

    @Column(name = "imageurl", length = 255)
    private String imageUrl;

}
