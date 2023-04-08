package com.mineservice.domain.tag.domain;

import com.mineservice.domain.article_tag.domain.ArticleTagEntity;
import com.mineservice.domain.user.domain.UserInfoEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@ToString(exclude = {"userInfo", "articleTag"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String name;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserInfoEntity userInfo;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    List<ArticleTagEntity> articleTag;

    @Builder
    protected TagEntity(String userId, String name, String createBy) {
        this.userId = userId;
        this.name = name;
        this.createBy = createBy;
        this.createDt = LocalDateTime.now();
    }
}
