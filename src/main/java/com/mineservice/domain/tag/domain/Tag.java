package com.mineservice.domain.tag.domain;

import com.mineservice.domain.article_tag.domain.ArticleTag;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String name;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private LocalDateTime createDt = LocalDateTime.now();

    @OneToMany(mappedBy = "article")
    List<ArticleTag> articleTags;

    @Builder
    protected Tag(String userId, String name, String createBy) {
        this.userId = userId;
        this.name = name;
        this.createBy = createBy;
    }
}
