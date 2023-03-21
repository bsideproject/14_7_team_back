package com.mineservice.domain.file_info.domain;

import com.mineservice.domain.article.domain.Article;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInfo {

    @Id
    private Long id;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "save_name")
    private String saveName;

    @Column(name = "org_name")
    private String orgName;

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
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article article;
}
