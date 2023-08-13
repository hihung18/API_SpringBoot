package com.example.api_java.security;

import com.example.api_java.security.jwt.JwtAuthEntryPoint;
import com.example.api_java.security.jwt.JwtAuthTokenFilter;
import com.example.api_java.security.jwt.JwtUtils;
import com.example.api_java.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthEntryPoint unauthorizedHandler;

    private final JwtUtils jwtUtils;

    public WebSecurityConfig(
            UserDetailsServiceImpl userDetailsService,
            JwtAuthEntryPoint unauthorizedHandler,
            JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        // TODO
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    // cấu hình phân quyền
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/auth/**",
                        "/api/v1/public**",
                        "/swagger-ui**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                .permitAll()
                .antMatchers(HttpMethod.GET,
                        "/api/categories**",
                        "/api/categories/**",
                        "/api/images**",
                        "/api/images/**",
                        "/api/rates**",
                        "/api/rates/**",
                        "/api/features**",
                        "/api/features/**",
                        "/api/featureTypes**",
                        "/api/featureTypes/**",
                        "/api/products/**",
                        "/api/products**",
                        "/api/event/**",
                        "/api/event**")
                .permitAll()
                .antMatchers(HttpMethod.GET,
                        "/api/auth/changePass**",
                        "/api/orderDetailViews**",
                        "/api/users/**",
                        "/api/users**")
                .hasAnyRole("USER", "SHOP", "ADMIN")
                .antMatchers(HttpMethod.DELETE,
                        "/api/categories/**",
                        "/api/images/**",
                        "/api/users/**",
                        "/api/features/**",
                        "/api/featureTypes/**",
                        "/api/products/**",
                        "/api/event/**")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,
                        "/api/categories/**",
                        "/api/images/**",
                        "/api/features/**",
                        "/api/featureTypes/**",
                        "/api/products/**",
                        "/api/event/**")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,
                        "/api/orders**",
                        "/api/orderDetails**",
                        "/api/rates**",
                        "/api/featureTypes**")
                .hasRole("USER")
                .antMatchers(HttpMethod.PUT,
                        "/api/users/**",
                        "/api/orders/**",
                        "/api/orderDetails/**",
                        "/api/rates/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE,
                        "/api/orders/**",
                        "/api/orderDetails/**",
                        "/api/rates/**")
                .hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}