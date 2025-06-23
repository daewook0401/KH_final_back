package com.nomnom.onnomnom.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfigure {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(HttpMethod.GET).permitAll();
                    requests.requestMatchers(HttpMethod.POST).permitAll();
                    requests.requestMatchers(HttpMethod.GET).authenticated();
                    requests.requestMatchers(HttpMethod.POST).authenticated();
                    requests.requestMatchers(HttpMethod.PUT).authenticated();
                    requests.requestMatchers(HttpMethod.PATCH).authenticated();
                    requests.requestMatchers(HttpMethod.DELETE).authenticated();
                })
                .build();
    }
}
