package com.mineservice.domain.article.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@ToString
public class Article {

    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "user_id")
    private String userId;

    private String title;

    private String type;

    private String url;

    private String favorite;

    @Column(name = "read_yn")
    private String readYn;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "modify_dt")
    private LocalDateTime modifyDt;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    public Article() {

    }
}
