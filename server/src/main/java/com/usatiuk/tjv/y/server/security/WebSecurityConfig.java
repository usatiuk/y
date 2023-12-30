package com.usatiuk.tjv.y.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usatiuk.tjv.y.server.dto.ErrorTo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Component
    class ErrorAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
                throws IOException {

            var err = new ErrorTo(List.of("Authentication failed"), HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            OutputStream responseStream = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(responseStream, err);
            responseStream.flush();
        }
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, HandlerMappingIntrospector introspector, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/post/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/post*")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/person")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/person")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/person/by-username/*")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/person/by-uuid/*")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/token")).permitAll()
                        .requestMatchers(mvc.pattern("/error")).permitAll()
                        .anyRequest().hasAuthority(UserRoles.ROLE_USER.name()))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowedMethods(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
