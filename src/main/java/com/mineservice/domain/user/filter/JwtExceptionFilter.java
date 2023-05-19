package com.mineservice.domain.user.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mineservice.global.exception.CustomException;
import com.mineservice.global.exception.ErrorCode;
import com.mineservice.global.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res); // go to 'JwtAuthenticationFilter'
        } catch (CustomException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, res, ex.getErrorCode());
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse res, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        res.setStatus(status.value());
        res.setContentType("application/json; charset=UTF-8");


        res.getWriter().write(objectMapper.writeValueAsString(
                ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .build()
        ));
    }
}