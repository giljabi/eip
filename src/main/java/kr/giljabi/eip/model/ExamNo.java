package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter @Setter
@ToString
@Entity
@Table(name = "examno")
public class ExamNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "examyear_id", nullable = false)
    private ExamYear examYear;

    @Column(nullable = false)
    private String name;

    @Column(name = "examday", length = 32)
    private String examDay;


}