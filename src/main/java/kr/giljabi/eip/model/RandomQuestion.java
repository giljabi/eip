package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "randomquestion")
public class RandomQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createat", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "uuid", nullable = false, length = 36)
    private String uuid;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    private String remoteip;

    // Getters and Setters
    // 생성자, toString, hashCode, equals 메서드도 필요에 따라 추가하세요.
}
