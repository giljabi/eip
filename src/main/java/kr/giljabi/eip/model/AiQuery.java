package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "aiquery")
public class AiQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "aimodel", length = 64, nullable = false)
    private String aiModel;

    @Column(name = "answer", length = 4096)
    private String answer;

    @Column(nullable = false)
    private int promptTokens = 0;
    @Column(nullable = false)
    private int completionTokens = 0;
    @Column(nullable = false)
    private int totalTokens = 0;
    @Column(nullable = false)
    private int responseTime = 0;

}
