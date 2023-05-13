package com.client.ws.rasmooplus.configuration;

import com.client.ws.rasmooplus.filter.AuthenticationFilter;
import com.client.ws.rasmooplus.repository.UserDetailsRepository;
import com.client.ws.rasmooplus.service.implementations.TokenServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Autowired
    private TokenServiceImplementation tokenService;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AuthenticationFilter(tokenService, userDetailsRepository), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurityCustomizer-> webSecurityCustomizer.ignoring()
                .requestMatchers(HttpMethod.GET, "/subscription-type")
                .requestMatchers(HttpMethod.GET, "/subscription-type/*")
                .requestMatchers(HttpMethod.POST, "/subscription-type*")
                .requestMatchers(HttpMethod.POST, "/users")
                .requestMatchers(HttpMethod.GET, "/user/*")
                .requestMatchers(HttpMethod.POST, "/payment/process")
                .requestMatchers(HttpMethod.POST, "/auth");
    }

}
