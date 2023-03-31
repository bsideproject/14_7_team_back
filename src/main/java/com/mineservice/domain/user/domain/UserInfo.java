package com.mineservice.domain.user.domain;

import com.mineservice.domain.article.domain.Article;
import com.mineservice.domain.tag.domain.Tag;
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
@Table(name = "user_info")
public class UserInfo {

    @Id
    @Column(name = "id")
    private String id;

    private String provider;

    private String uid;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "last_login_dt")
    private LocalDateTime lastLoginDt;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "modify_dt")
    private LocalDateTime modifyDt;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @OneToMany(mappedBy = "userInfo")
    private List<Article> articles;

    @OneToMany(mappedBy = "userInfo")
    private List<Tag> tags;

    @OneToOne
    @JoinColumn(name = "id")
    private UserAlarm userAlarm;

    @OneToOne
    @JoinColumn(name = "id")
    private AccessToken accessToken;

    @OneToOne
    @JoinColumn(name = "id")
    private RefreshToken refreshToken;

    @Builder
    public UserInfo(String id, String provider, String uid, String userEmail, String userName, String modifyBy, LocalDateTime modifyDt, String createBy) {
        this.id = id;
        this.provider = provider;
        this.uid = uid;
        this.userEmail = userEmail;
        this.userName = userName;
        this.lastLoginDt = LocalDateTime.now();
        this.modifyBy = modifyBy;
        this.modifyDt = modifyDt;
        this.createBy = createBy;
        this.createDt = LocalDateTime.now();
    }
}
