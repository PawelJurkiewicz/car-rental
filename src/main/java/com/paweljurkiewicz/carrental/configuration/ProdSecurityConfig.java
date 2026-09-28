package com.paweljurkiewicz.carrental.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class ProdSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
                .authorizeHttpRequests(authorizationMatcher ->
                        authorizationMatcher
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/actuator/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/cars/**", "/branches/**", "/car_rentals/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/cars/**", "/car_rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/reservations/**", "/rents/**").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated()
                );
        return httpSecurity.build();
    }
}
