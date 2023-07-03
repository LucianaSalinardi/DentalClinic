package ar.com.dh.login;

import ar.com.dh.login.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;

    public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    public void configure(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        auth.authenticationProvider(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/clinical-dental/appointments/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/clinical-dental/appointments/**").hasAuthority("USER")
                .requestMatchers(HttpMethod.POST, "/clinical-dental/appointments/**").hasAuthority("USER")
                .requestMatchers(HttpMethod.PUT, "/clinical-dental/appointments/**").hasAuthority("USER")
                .requestMatchers(HttpMethod.DELETE, "/clinical-dental/appointments/**").hasAuthority("USER")


                .requestMatchers("/clinical-dental/patients/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/clinical-dental/patients/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/clinical-dental/patients/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/clinical-dental/patients/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/clinical-dental/patients/**").hasAuthority("ADMIN")

                .requestMatchers("/clinical-dental/dentists/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/clinical-dental/dentists/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/clinical-dental/dentists/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/clinical-dental/dentists/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/clinical-dental/dentists/**").hasAuthority("ADMIN")
                .anyRequest().authenticated().and().formLogin(form -> form.defaultSuccessUrl("/system.html", true)).logout();


        http.httpBasic();
        return http.build();
    }
}
