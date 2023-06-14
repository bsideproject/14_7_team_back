package com.mineservice.domain.action.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "action_hist")
public class ActionHistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String type;

    private String detail;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Builder
    public ActionHistEntity(Long id, String userId, String type, String detail, LocalDateTime regDt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.detail = detail;
        this.regDt = regDt;
    }
}
