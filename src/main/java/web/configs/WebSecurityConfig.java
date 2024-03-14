package web.configs;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import web.service.UserService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final LoginSuccessHandler successHandler;

    private final UserDetailsService userDetailsService;
    private final SecurityBeansConfig securityBeansConfig;

    public WebSecurityConfig(UserService userService, LoginSuccessHandler successHandler, UserDetailsService userDetailsService, SecurityBeansConfig securityBeansConfig) {
        this.userService = userService;
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;
        this.securityBeansConfig = securityBeansConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successHandler).permitAll()
                .and()
                .logout()
                .logoutUrl("/logout");

    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable() // Отключение CSRF защиты
//                .authorizeRequests()
//                .anyRequest().permitAll(); // Разрешить все запросы
//    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(securityBeansConfig.getPasswordEncoder());
    }
}