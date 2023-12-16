package com.example.essentialnews.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(new HandlerMappingIntrospector());
        http.authorizeHttpRequests((authz) ->
                    authz
                            .requestMatchers(mvcMatcherBuilder.pattern("/index"),
                                    mvcMatcherBuilder.pattern("/createUser"),
                                    mvcMatcherBuilder.pattern("/datalab")
                                    ).permitAll()
                            .anyRequest().permitAll()
                );
        http.formLogin(Customizer.withDefaults());
        //http.formLogin(login -> login.successForwardUrl("/homepage"));
        http.httpBasic(Customizer.withDefaults());

        return http.build();

    }

}
