package com.mineservice.domain.user;

import com.mineservice.domain.user.domain.MineKeyEntity;
import com.mineservice.domain.user.repository.MineKeyRepository;
import com.mineservice.domain.user.service.MineKeyService;
import io.jsonwebtoken.*;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    private final MineKeyService mineKeyService;

    @Value("${token.secret}")
    private String SECRET_KEY;


    @Value("${token.expiration_time}")
    private Long TOKEN_EXPIRATION_TIME;

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String generateJwt(String userId, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", roles);

        Date expiration = new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME);
        log.info("expiration : {}", expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        Optional<MineKeyEntity> optionalMineKey = mineKeyService.findByUserId(userId);
        if (optionalMineKey.isPresent()) {
            log.info("mine key update");
            mineKeyService.updateKey(optionalMineKey.get(), userId, token);
        } else {
            log.info("mine key create");
            mineKeyService.createKey(userId, token);
        }

        return token;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        String userId = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        log.info("token parse userId : {}", userId);
        return userId;
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
        log.info("valid token");
        return !claims.getBody().getExpiration().before(new Date());
    }
}
