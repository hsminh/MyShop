package com.example.myshopdaknong.sercurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserSecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(DetailService deltailSerVice)
    {
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(deltailSerVice);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                .authorizeHttpRequests(configurer -> configurer

                        .requestMatchers("/login","/main-page","/users/save","/users/check-username-unique","/users/register").permitAll()
                        .requestMatchers("/users/**").hasAnyAuthority("Admin","User")
                        .requestMatchers("/category/**").hasAnyAuthority("Admin")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin")
                        .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll() // Đường dẫn tĩnh được phép truy cập mà không cần xác thực
                        .anyRequest().authenticated()
                ).formLogin(
                        form->form.loginPage("/login-form").loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/main-page",true)
                                .permitAll()
                                .usernameParameter("username")
                                .passwordParameter("password")
                ).logout(
                       logout->logout.logoutUrl("/logout")
                               .logoutSuccessUrl("/login")
                               .invalidateHttpSession(true) // Invalidates the HttpSession
                               .deleteCookies("JSESSIONID")
                ).rememberMe(
                        rememberme->rememberme
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(604800)
                .key("uniqueAndSecret")
                ).exceptionHandling(
                        exceptionHandling->exceptionHandling
                                .accessDeniedPage("/access-denied")
                )
        ;
        return security.build();
    }
}
