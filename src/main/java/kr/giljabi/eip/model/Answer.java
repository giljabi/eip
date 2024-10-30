package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
*/

    @Column(nullable = false)
    private Integer correct;

    private Long examnoId;

}