package com.mineservice.login.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_info")
public class User extends BaseEntity{

    @Id
    private String id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(nullable = false)
    private String provider;

    @Column(name = "uid")
    private String uid;

    @Column(name = "last_login_dt")
    private LocalDateTime lastLoginDt;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "create_by")
    private String createBy;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;

    public User update(String name) {
        this.name = name;
        return this;
    }

//    public String getRoleKey() {
//        return this.role.getKey();
//    }

}
