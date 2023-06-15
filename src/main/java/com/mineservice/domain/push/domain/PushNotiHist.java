package com.mineservice.domain.push.domain;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "push_noti_hist")
public class PushNotiHist {

    @Id
    private Long id;

    private String title;

    private String body;

    @Column(name = "success_count")
    private Long successCount;

    @Column(name = "fail_count")
    private Long failCount;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @Builder
    public PushNotiHist(Long id, String title, String body, Long successCount, Long failCount, LocalDateTime createDt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.successCount = successCount;
        this.failCount = failCount;
        this.createDt = createDt;
    }
}
