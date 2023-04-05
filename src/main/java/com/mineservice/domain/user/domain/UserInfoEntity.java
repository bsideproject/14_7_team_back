package com.mineservice.domain.user.domain;

import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.tag.domain.TagEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "user_info")
public class UserInfoEntity implements UserDetails {

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
    private List<ArticleEntity> article;

    @OneToMany(mappedBy = "userInfo")
    private List<TagEntity> tag;

    @OneToOne
    @JoinColumn(name = "id")
    private UserAlarmEntity userAlarm;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private AccessTokenEntity accessToken;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private RefreshTokenEntity refreshToken;

    @Builder
    public UserInfoEntity(String id, String provider, String uid, String userEmail, String userName, String modifyBy, LocalDateTime modifyDt, String createBy) {
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

    @ElementCollection(fetch = FetchType.EAGER) //roles 컬렉션
    @CollectionTable(name = "user_info_role")
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public UserInfoEntity update(String name) {
        this.userName = name;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
