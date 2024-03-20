package com.example.myshopdaknong.Sercurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/users/save").permitAll() // Chỉnh sửa đây
                        .requestMatchers("/users/check-username-unique").permitAll() // Chỉnh sửa đây
                         .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/users/**").hasAnyAuthority("Admin","User")
                        .requestMatchers("/category/**").hasAnyAuthority("Admin","User")
                        .anyRequest().authenticated()
                ).formLogin(
                        form->form.loginPage("/login-form").loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/main-page",true)
                                .permitAll()
                                .usernameParameter("username")
                                .passwordParameter("password")
                );
        return security.build();
    }
}
