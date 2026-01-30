package com.newsgroupmanagement.security;

import com.newsgroupmanagement.service.CustomOidcUserService;
import com.newsgroupmanagement.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomOidcUserService oidcUserService;
    private final CustomLoginSuccessHandler successHandler;
    private final CustomLoginFailureHandler failureHandler;
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
    private final GoogleOAuth2FailureHandler googleOAuth2FailureHandler;
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomOidcUserService oidcUserService, CustomLoginSuccessHandler successHandler, CustomLoginFailureHandler failureHandler, GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler, GoogleOAuth2FailureHandler googleOAuth2FailureHandler) {
        this.userDetailsService = userDetailsService;
        this.oidcUserService = oidcUserService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
        this.googleOAuth2FailureHandler = googleOAuth2FailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Create and configure the custom filter
        CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter();
        customFilter.setAuthenticationFailureHandler(failureHandler);

        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/","/api/news/home","/api/news/details/**","/api/news/search","/api/news/stream","/auth/**","/public/**","/contact/**","/css/**","/js/**","/uploads/images/**").permitAll()
                        .requestMatchers("/user/**","/api/news/**").hasAnyRole("USER","ADMIN","MASTER_ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN","MASTER_ADMIN")
                        .requestMatchers("/master/manage-admins/**").hasRole("MASTER_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/auth/sign-in")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService)
                        )
                        .successHandler(googleOAuth2SuccessHandler)
                        .failureHandler(googleOAuth2FailureHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
