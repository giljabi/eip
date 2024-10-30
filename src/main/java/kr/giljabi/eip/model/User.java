package kr.giljabi.eip.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "userinfo", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "user_nm", nullable = false, length = 20)
    private String userName;

    @Column(name = "user_pw", nullable = false, length = 100)
    private String userPassword;

    @Column(name = "use_flag", nullable = false, length = 1)
    private String useFlag;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 20)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 20)
    private String updatedBy;

}