package com.mineservice.domain.user.filter;

import com.mineservice.domain.user.JwtTokenProvider;
import com.mineservice.global.exception.CustomException;
import com.mineservice.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private final String TOKEN_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (token != null) {
            token = token.replace(TOKEN_PREFIX, "");

            try {
                jwtTokenProvider.validateToken(token);
                log.info("validateToken");
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (IllegalArgumentException e) {
                log.error("an error occured during getting username from token", e);
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            } catch (ExpiredJwtException e) {
                log.warn("the token is expired and not valid anymore", e);
                throw new CustomException(ErrorCode.EXPIRED_TOKEN);
            } catch (SignatureException e) {
                log.error("Authentication Failed. Username or Password not valid.");
                throw new CustomException(ErrorCode.INVALID_SIGNATURE);
            } catch (MalformedJwtException e) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
        }

        chain.doFilter(request, response);

    }
}
