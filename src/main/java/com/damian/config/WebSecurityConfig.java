package com.damian.config;

import com.damian.security.Http401UnauthorizedEntryPoint;
import com.damian.security.jwt.JWTConfigurer;
import com.damian.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static String inMemoryUsername;
    private static String inMemoryPassword;


    @Value("${app.security.inMemoryUsername}")
    String username;

    @Value("${app.security.inMemoryPassword}")
    String password;

    @PostConstruct
    public void initStaticInMemoryUserFields() {
        WebSecurityConfig.inMemoryUsername = username;
        WebSecurityConfig.inMemoryPassword = password;
    }

    @Configuration
    @Order(2)
    public static class ApiWebSecurityHttpBasic0 extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/extbaskets","/basketsextlist","/basket_ext_stock")
                .authenticated().and().httpBasic();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            auth.inMemoryAuthentication()
                .withUser(inMemoryUsername)
                .password(encoder.encode(inMemoryPassword))
                .roles("admin");
        }
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityJWT extends WebSecurityConfigurerAdapter {

        private final TokenProvider tokenProvider;
        public ApiWebSecurityJWT(TokenProvider tokenProvider) {
            this.tokenProvider = tokenProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().configurationSource(corsConfigurationSource()).and()
                .exceptionHandling().authenticationEntryPoint(http401UnauthorizedEntryPoint()).and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers("/auth_loyalty_program").permitAll()
                .antMatchers("/program_users/resetpassword/*").permitAll()
                .antMatchers("/notification").permitAll()
                .antMatchers("/new_order_notification").permitAll()
               .anyRequest().authenticated()
                .and().apply(securityConfigurerAdapter());


        }

        private JWTConfigurer securityConfigurerAdapter() {
            return new JWTConfigurer(tokenProvider);
        }


        @Bean
        @Override
        protected AuthenticationManager authenticationManager() throws Exception {
            return super.authenticationManager();
        }

        @Bean
        public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
            return new Http401UnauthorizedEntryPoint();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return source;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}



