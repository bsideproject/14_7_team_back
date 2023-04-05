package com.mineservice.domain.push.domain;

import javax.persistence.Table;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "device")
public class DeviceToken {

    @Id
    private String token;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "fail_count")
    private int failCount;

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

    @Builder
    public DeviceToken(String token, String userId, int failCount, String useYn, String modifyBy, LocalDateTime modifyDt, String createBy, LocalDateTime createDt) {
        this.token = token;
        this.userId = userId;
        this.failCount = failCount;
        this.useYn = useYn;
        this.modifyBy = modifyBy;
        this.modifyDt = modifyDt;
        this.createBy = createBy;
        this.createDt = createDt;
    }
}
