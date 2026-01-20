package com.example.shop.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        log.info("---------------securityFilterChain---------------------------");

        http
                .formLogin(login -> login
                        .loginPage("/members/login")
                        .loginProcessingUrl("/members/login")
                        .failureUrl("/members/login/error")
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("email") //name: username 기입할 필요 없음
                        //.passwordParameter("pwd") //만약 name: pasword아니고 name: pwd
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/members/logout")
                        .logoutSuccessUrl("/")
                );



        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
