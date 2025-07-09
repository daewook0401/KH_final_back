package com.nomnom.onnomnom.global.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nomnom.onnomnom.global.config.filter.CoopFilter;
import com.nomnom.onnomnom.global.config.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfigure {

    private final JwtFilter jwtFilter;
    private final CoopFilter coopFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(HttpMethod.POST, "/api/test", "/api/member/mypage-info", "/api/auth/password-confirm").authenticated();
                    requests.requestMatchers(HttpMethod.DELETE, "/api/auth/logout", "/api/reservation/**","/api/operatings/**","/api/settings/**","/api/restaurants/**").authenticated();
                    //requests.requestMatchers(HttpMethod.DELETE, "/restaurants/**").permitAll();
                    //requests.requestMatchers(HttpMethod.GET).authenticated();
                    requests.requestMatchers(HttpMethod.PUT, "/api/member/social-update", "/api/member/update","/api/restaurants/**").authenticated();
                    requests.requestMatchers(HttpMethod.PATCH).authenticated();
                    requests.requestMatchers(HttpMethod.GET).permitAll();
                    requests.requestMatchers(HttpMethod.POST).permitAll();
                })
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(coopFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
		CorsConfiguration configuration = new CorsConfiguration();
		// configuration.setAllowedOrigins(Arrays.asList("192.168.219.**:**"));
		configuration.setAllowedOriginPatterns(Arrays.asList("https://onnomnom.shop"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
		return authConfig.getAuthenticationManager();
	}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
