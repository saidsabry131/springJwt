package org.example.springjwt.config;

import org.example.springjwt.jwt.JwtFilter;
import org.example.springjwt.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(MyUserDetailsService myUserDetailsService, JwtFilter jwtFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(
                request ->
                        request.requestMatchers("/").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/user/**").hasAuthority("USER")
                                .anyRequest().authenticated()
        );
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.authenticationProvider(provider());
        httpSecurity.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
      //  httpSecurity.formLogin(Customizer.withDefaults());


        return httpSecurity.build();
    }


    public AuthenticationProvider provider()
    {
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncode());
        provider.setUserDetailsService(myUserDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncode() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
      //  return new ProviderManager(provider());
        return configuration.getAuthenticationManager();
    }
}
