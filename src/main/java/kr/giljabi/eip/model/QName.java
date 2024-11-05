package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "qname")
public class QName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false)
    private String name;

    // 기본 생성자
    public QName(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // 생성자
    public QName(String name) {
        this.name = name;
    }

    public QName() {

    }
}


