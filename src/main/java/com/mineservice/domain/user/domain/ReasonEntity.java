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
@Table(name = "reason")
public class ReasonEntity {

    @Id
    private int id;

    @Column(name = "user_id")
    private String userId;

    private String type;

    private String reason;

    @Column(name = "created_dt")
    private LocalDateTime createdDt;

}
