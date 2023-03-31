package com.mineservice.domain.user;

import com.mineservice.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUidAndProvider(String id, String provider);
}
