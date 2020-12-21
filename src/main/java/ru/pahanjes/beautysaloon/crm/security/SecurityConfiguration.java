package ru.pahanjes.beautysaloon.crm.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import ru.pahanjes.beautysaloon.crm.backend.service.UserService;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /*
     * URL to enter the account in process
     */
    private static final String LOGIN_PROCESSING_URL = "/login";
    /*
     * URL to enter the account
     */
    private static final String LOGIN_URL = "/logout";
    /*
     * URL to first page after logout
     */
    private static final String LOGOUT_SUCCESS_URL = "/login";

    private final UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Отключает защиту от подделки межсайтовых запросов (CSRF), так как Vaadin уже имеет защиту CSRF.
        http.csrf().disable()
                // Использует CustomRequestCache для отслеживания неавторизованных запросов,
                // Чтобы пользователи перенаправлялись соответствующим образом после входа в систему.
                .requestCache().requestCache(new CustomRequestCache())
                // Включает авторизацию.
                .and()
                    .authorizeRequests()
                    // Разрешает весь внутренний трафик из фреймворка Vaadin.
                    .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                    // Разрешает весь аутентифицированный трафик.
                    .anyRequest().authenticated()
                // Включает вход на основе формы и разрешает неаутентифицированный доступ к нему.
                .and().formLogin()
                    .loginPage(LOGIN_URL).permitAll()
                    // Настраивает URL-адреса страницы входа.
                    .loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .defaultSuccessUrl("/lk")
                // Настраивает URL-адрес выхода.
                .and()
                    .logout().
                    logoutSuccessUrl(LOGOUT_SUCCESS_URL)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
        ;
    }

    // Исключение связь Vaadin-framework и статических активов из Spring Security.
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/frontend/**"
        );
    }

}