package com.mineservice.domain.user.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @LastModifiedDate
    @Column(name = "modify_dt")
    private LocalDateTime modifyDt;

    @CreatedDate
    @Column(name = "create_dt", updatable = false)
    private LocalDateTime createDt;
}
