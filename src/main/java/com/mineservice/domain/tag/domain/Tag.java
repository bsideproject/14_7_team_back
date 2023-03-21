package com.mineservice.domain.tag.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@ToString
public class Tag {

    @Id
    private Long id;

    @JoinColumn(name = "user_id")
    @Column(name = "user_id")
    private String userId;

    private String name;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    public Tag() {

    }
}
