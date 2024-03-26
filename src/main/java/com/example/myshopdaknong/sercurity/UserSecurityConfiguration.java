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
                        //url ko xac thuc
                        .requestMatchers("/login","/public/images/**","/main-page","/users/save","/users/check-username-unique","/users/register").permitAll()
                        .requestMatchers("/users/**").hasAnyAuthority("Admin","User")
                        .requestMatchers("/category/**").hasAnyAuthority("Admin")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin")

                        .anyRequest().authenticated()
                ).formLogin(
                        form->form.loginPage("/login-form").loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/main-page",true)
                                .permitAll()
                                .usernameParameter("username")
                                .passwordParameter("password")
                ).logout(
                       logout->logout.logoutUrl("/logout") // URL để logout
                               .logoutSuccessUrl("/login") // URL chuyển hướng sau khi logout thành công
                               .invalidateHttpSession(true) // Invalidates the HttpSession
                               .deleteCookies("JSESSIONID") // Xóa cookie (nếu cần)
                );
        return security.build();
    }
}
