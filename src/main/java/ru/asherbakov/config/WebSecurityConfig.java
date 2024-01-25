package ru.asherbakov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Отключаем CSRF для активации возможности отправлять AJAX на контроллер (можно в AJAX добавить CSRF, но пока не разобрался)
        http.csrf().disable();
        http
                // Доступ к главной странице разрешен всем, на остальные - требовать авторизацию
                .authorizeRequests()
                .antMatchers("/admin/").hasAuthority("ADMIN") //hasAnyRole("ADMIN", "USER") hasRole("ADMIN") hasAuthority("ADMIN")
                .antMatchers("/css/**", "/webfonts/**", "/images/**", "/js/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")    // адрес страницы авторизации
                .permitAll()
                .and()
                .logout()
                .permitAll();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // не шифруем пароли (требование ТЗ)
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                // username, password, active - обязательные парамметры, active заменил на 'true'
                .usersByUsernameQuery("SELECT username, password, active FROM user WHERE username=?")
                // получение роли авторизуемого пользователя
                .authoritiesByUsernameQuery("SELECT u.username, r.name FROM user u INNER JOIN role r on u.role_id = r.id WHERE u.username=?");
    }
}
