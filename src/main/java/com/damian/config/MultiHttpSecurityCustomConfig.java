package com.damian.config;

import com.damian.security.Http401UnauthorizedEntryPoint;
import com.damian.security.jwt.JWTConfigurer;
import com.damian.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class MultiHttpSecurityCustomConfig {


    private final UserDetailsService userDetailsService;

    public MultiHttpSecurityCustomConfig(UserDetailsService userDetailsService ) {
        this.userDetailsService = userDetailsService;

    }



    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        auth.inMemoryAuthentication()
                .withUser("pass")
                .password("pass")
                .roles("admin");

    }


    @Configuration
    @Order(1)
    public static class ApiWebSecurityHttpBasic extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/extbaskets").authorizeRequests().anyRequest().hasRole("admin").and().httpBasic();
                   //http.csrf().disable().antMatcher("/**").authorizeRequests().antMatchers("/extbaskets","/basketsextlist","/basket_ext_stock").authenticated().and().httpBasic();

        }
    }

    @Configuration
    @Order(2)
    public static class ApiWebSecurityHttpBasic2 extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/basketsextlist").authorizeRequests().anyRequest().hasRole("admin").and().httpBasic();
            //http.csrf().disable().antMatcher("/**").authorizeRequests().antMatchers("/extbaskets","/basketsextlist","/basket_ext_stock").authenticated().and().httpBasic();

        }
    }

    @Configuration
    @Order(3)
    public static class ApiWebSecurityHttpBasic3 extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/basket_ext_stock").authorizeRequests().anyRequest().hasRole("admin").and().httpBasic();
            //http.csrf().disable().antMatcher("/**").authorizeRequests().antMatchers("/extbaskets","/basketsextlist","/basket_ext_stock").authenticated().and().httpBasic();

        }
    }


    @Configuration
    public static class ApiWebSecurityJWT extends WebSecurityConfigurerAdapter {

        private final TokenProvider tokenProvider;
        public ApiWebSecurityJWT(TokenProvider tokenProvider) {
            this.tokenProvider = tokenProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and()
                    .exceptionHandling().authenticationEntryPoint(http401UnauthorizedEntryPoint()).and()
                    .csrf()
                    .disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/auth").permitAll()
                    .anyRequest().authenticated()
                    .and().apply(securityConfigurerAdapter())
            ;
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

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
