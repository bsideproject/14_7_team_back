package com.mineservice.domain.user.domain;

import com.mineservice.login.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String token;

    @Column(name = "expire_dt")
    private LocalDateTime expireDt;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "create_by")
    private String createBy;

}
