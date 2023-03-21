package com.mineservice.global.auth;

import com.mineservice.domain.user.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/user").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().accessDeniedPage("/accessDenied")
            .and()
            .logout().logoutUrl("/logout")
            .logoutSuccessUrl("/").permitAll()
            .and()
            .oauth2Login().loginPage("/login")
            .userInfoEndpoint()
            .userService(customOAuth2UserService);

    }
}
