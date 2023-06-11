package com.mineservice.domain.article.domain;

import com.mineservice.domain.article_tag.domain.ArticleTagEntity;
import com.mineservice.domain.file_info.domain.FileInfoEntity;
import com.mineservice.domain.user.domain.UserInfoEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@ToString(exclude = {"userInfo", "articleTag", "fileInfoEntities", "articleAlarm"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "article")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserInfoEntity userInfo;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    List<ArticleTagEntity> articleTag;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<FileInfoEntity> fileInfoEntities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private ArticleAlarm articleAlarm;

    @Builder
    protected ArticleEntity(String userId, String title, String type, String url, String favorite, String modifyBy, LocalDateTime modifyDt, String createBy) {
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.url = url;
        this.favorite = favorite;
        this.readYn = "N";
        this.useYn = "Y";
        this.modifyBy = modifyBy;
        this.modifyDt = modifyDt;
        this.createBy = createBy;
        this.createDt = LocalDateTime.now();
    }
}
