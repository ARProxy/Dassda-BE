package com.dassda.config;

import com.dassda.jwt.JwtAuthenticationFilter;
import com.dassda.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) -> headerConfig
                        .frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/member/**", "https://localhost:3000/**").permitAll()
                                .requestMatchers("/login/**", "/index/**", "/images/**", "/**", "/app/**").permitAll()
                                .requestMatchers("/oauth/**", "/swagger-ui/**", "/v3/api-docs/**", "/v2/api-docs/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll()
                                .requestMatchers("/v2/user/**").permitAll()
                                .requestMatchers("https://ssda-front-tan.vercel.app/", "https://dassda.today", "https://www.dassda.today").permitAll()
                                .anyRequest().authenticated()
                )

                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//                            try {
//                                authorizationManagerRequestMatcherRegistry
//                                        .requestMatchers(HttpMethod.POST, POST_AUTH_WHITELIST).permitAll()
//                                        .requestMatchers(HttpMethod.GET, GET_AUTH_WHITELIST).permitAll()
//                                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                                        .anyRequest()
//                                        .authenticated()
//                                        .and()
//                                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                            } catch (Exception e) {
//                                throw new ResourceNotFoundException(e.getMessage());
//                            }
//                        }
//                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000", "https://ssda-front-tan.vercel.app/", "https://dassda.today/", "https://www.dassda.today/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtTokenProvider));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
