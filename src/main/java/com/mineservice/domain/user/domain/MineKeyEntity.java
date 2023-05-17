package com.mineservice.domain.user.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "mine_key")
public class MineKeyEntity {

    @Id
    private int id;

    @Column(name = "user_id")
    private String userId;

    private String token;

    @Column(name = "expire_dt")
    private LocalDateTime expireDt;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "modify_dt")
    private LocalDateTime modifyDt;

}
