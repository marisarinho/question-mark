package br.edu.ifpb.pweb2.question_mark.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class QuestionMarkSecurityConfig {

    @Autowired
    private DataSource dataSource; 

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/imagens/**", "/login", "/cadastro").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN") 
                .requestMatchers("/corridas/**").hasRole("PARTICIPANTE") 
                .anyRequest().authenticated()
            )
           .formLogin(form -> form
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                    if (isAdmin) {
                        response.sendRedirect("/admin/corridas");
                    } else {
                        response.sendRedirect("/home");
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado")); 
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN").build();

        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        
        if (users.userExists(admin.getUsername())) {
            users.updateUser(admin);
        } else {
            users.createUser(admin);
        }
        return users;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
