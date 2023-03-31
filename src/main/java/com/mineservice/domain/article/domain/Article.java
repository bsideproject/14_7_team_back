package com.mineservice.domain.article.domain;

import com.mineservice.domain.article_tag.domain.ArticleTag;
import com.mineservice.domain.file_info.domain.FileInfo;
import com.mineservice.domain.user.domain.UserInfo;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Article {

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

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "article")
    List<ArticleTag> articleTags;

    @OneToMany(mappedBy = "article")
    private List<FileInfo> fileInfos;

    @OneToOne
    @JoinColumn(name = "id")
    private ArticleAlarm articleAlarm;

    @Builder
    protected Article(String userId, String title, String type, String url, String favorite, String modifyBy, LocalDateTime modifyDt, String createBy) {
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