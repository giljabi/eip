package kr.giljabi.eip.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tokenusage", uniqueConstraints = {@UniqueConstraint(columnNames = "date")})
public class TokenUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;    // yyyy-MM-dd

    @Column(nullable = false)
    private int promptTokens = 0;
    @Column(nullable = false)
    private int completionTokens = 0;
    @Column(nullable = false)
    private int totalTokens = 0;
    @Column(nullable = false)
    private int reqcnt = 0;

    public TokenUsage(String date, int promptTokens, int completionTokens,
                      int totalTokens, int reqcnt) {
        this.date = date;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
        this.reqcnt = reqcnt;
    }

    public TokenUsage() {

    }
}
