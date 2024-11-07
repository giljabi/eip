package kr.giljabi.eip.config;

import kr.giljabi.eip.auth.LoginFailureHandler;
import kr.giljabi.eip.auth.LoginSuccessHandler;
import kr.giljabi.eip.auth.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .headers()
                        .xssProtection().block(true)
                .and()
                        .frameOptions().deny()
                .and()
                        .cors()
                .and()
                        .csrf().disable()
                .authorizeRequests()
                .antMatchers("/index.html", "/css/**", "/js/**", "/login.html",
                        "/questions/**", "/user/login").permitAll()
                // /user/** 경로는 로그인한 사용자만 접근 가능
                .antMatchers("/user/**", "/register/**").authenticated()
                .anyRequest().permitAll()
                .and();

        http
                .formLogin()
                        .loginPage("/user/login")
                        .loginProcessingUrl("/authenticate")
                        .successHandler(this.loginSuccessHandler)
                        .failureHandler(this.loginFailureHandler)
                        .usernameParameter("userId")
                        .passwordParameter("password")
                .permitAll();

        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/user/login")
                .invalidateHttpSession(true)
                .permitAll();
    }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }
}
