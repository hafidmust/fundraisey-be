package com.fundraisey.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true) //Enable @Secured annotation in Controller
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * Manage resource server.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    private static final String SECURED_PATTERN = "/api/**";

    /**
     * Manage endpoints.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/v1/forgot-password**", "/v1/forgot-password-reset**", "/v1/login**",
                        "/v1/register**", "/register**", "/register/send-email**","/api/user-register/web/**",
                        "/api/user-register/web/index/**", "/error**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll()
        ;
    }
}

