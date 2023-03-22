package com.mineservice.domain.article.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleAlarm {

    @Id
    @Column(name = "article_id")
    private Long articleId;

    private LocalTime time;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "modify_dt")
    private LocalDateTime modifyDt;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

}
