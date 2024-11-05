package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Table(name = "question", uniqueConstraints = @UniqueConstraint(columnNames = {"examno_id", "no"}))
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "examno_id", nullable = false)
    private ExamNo examNo;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    /*
        @OneToOne
        @JoinColumn(name = "answer_id", nullable = false)
        private Answer answer;
    */
    @Column(nullable = false)
    private Integer correct;    //정답

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer no;

    @Column(name="questionimageflag", nullable = false)
    private boolean questionImageFlag;

    @Column(name="imageurl")
    private String imageUrl;

    @Column(name="choiceimageflag", nullable = false)
    private boolean choiceImageFlag;

    @Column(name="replycount", nullable = false)
    private Integer replyCount;

    @Column(name="correctcount", nullable = false)
    private Integer correctCount;

    @Column(name="useflag")
    private boolean useFlag;

    @ManyToOne
    @JoinColumn(name = "qid", nullable = false)
    private QName qid;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Choice> choices;  // 각 Question이 여러 Choice를 가짐

}

