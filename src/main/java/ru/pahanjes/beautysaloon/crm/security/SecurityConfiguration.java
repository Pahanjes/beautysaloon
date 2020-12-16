package ru.pahanjes.beautysaloon.crm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /*
     * URL to enter the account in process
     */
    private static final String LOGIN_PROCESSING_URL = "/login";
    /*
     * URL to enter the account if an error was catched
     */
    private static final String LOGIN_FAILURE_URL = "/login?error";
    /*
     * URL to enter the account
     */
    private static final String LOGIN_URL = "/login";
    /*
     * URL to first page after logout
     */
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().requestCache(new CustomRequestCache())
                .and().authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                .anyRequest().authenticated()

                .and().formLogin()
                    .loginPage(LOGIN_URL).permitAll()
                    .loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .failureUrl(LOGIN_FAILURE_URL)
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
        ;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withUsername("user")
                        .password("{noop}password")
                        .roles("USER")
                        .build()
                ;

        return new InMemoryUserDetailsManager(user);
    }

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