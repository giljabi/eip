package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
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

    private String content;

    @ManyToOne
    @JoinColumn(name = "qid", nullable = false)
    private QName qid;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    //@JsonIgnore //toString에서 순환참조가 발생하므로 json으로 변환할 때 무시하는 방법
    private List<Choice> choices = new ArrayList<>();  // 각 Question은 4개 선택지를 소유함

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", examNo=" + examNo +
                ", subject=" + subject +
                ", correct=" + correct +
                ", name='" + name + '\'' +
                ", no=" + no +
                ", questionImageFlag=" + questionImageFlag +
                ", imageUrl='" + imageUrl + '\'' +
                ", choiceImageFlag=" + choiceImageFlag +
                ", replyCount=" + replyCount +
                ", correctCount=" + correctCount +
                ", useFlag=" + useFlag +
                ", qid=" + qid +
                //", choices=" + choices + //순환참조 발생
                '}';
    }
}



