package com.mineservice.domain.push.domain;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "push_noti")
public class PushNoti {

    @Id
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "article_id")
    private Long articleId;

    @Column(name = "read_yn")
    private String readYn;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    @Builder
    public PushNoti(Long id, String userId, Long articleId, String readYn, LocalDate createdDate, LocalDateTime createdDt) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.readYn = readYn;
        this.createdDate = createdDate;
        this.createdDt = createdDt;
    }
}
