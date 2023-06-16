package com.mineservice.domain.article.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleAlarm {

    @Id
    @Column(name = "article_id")
    private Long articleId;

    private LocalDateTime time;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "modify_dt")
    private LocalDateTime modifyDt;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @Builder
    public ArticleAlarm(Long articleId, LocalDateTime time,String modifyBy,LocalDateTime modifyDt, String createBy, LocalDateTime createDt) {
        this.articleId = articleId;
        this.time = time;
        this.modifyBy = modifyBy;
        this.modifyDt = modifyDt;
        this.createBy = createBy;
        this.createDt = createDt;
    }
}
