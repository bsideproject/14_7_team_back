package com.mineservice.domain.push.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
