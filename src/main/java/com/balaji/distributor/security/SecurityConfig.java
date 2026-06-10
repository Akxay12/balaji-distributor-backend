package com.balaji.distributor.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(

            HttpSecurity http

    ) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .cors(Customizer.withDefaults())

                .sessionManagement(

                        session -> session

                                .sessionCreationPolicy(

                                        SessionCreationPolicy.STATELESS
                                )
                )

                .authorizeHttpRequests(auth ->

        auth

                .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                )
                .permitAll()

                .requestMatchers(

                        "/users/signup",

                        "/users/login",

                        "/products/view_all",

                        "/banner/get",

                        "/help/user/sendrequest",

                        "/checkout-session/public/**"

                )
                .permitAll()




                                .requestMatchers(

                                        HttpMethod.GET,

                                        "/store-settings"

                                )

                                .permitAll()

                                .requestMatchers(

                                        "/analytics/products",

                                        "/analytics/products/**",

                                        "/dashboard",

                                        "/banner/admin/save",

                                        "/help/admin/**",

                                        "/help/admin/review/*",

                                        "/help/admin/allrequests",

                                        "/orders/all",

                                        "/orders/*/status",

                                        "/products/admin/create",

                                        "/products/admin/**",

                                        "/users/admin/all",

                                        "/users/block/**",

                                        "/users/admin/activity"

                                )

                                .hasRole("ADMIN")

                                .requestMatchers(

                                        HttpMethod.PUT,

                                        "/store-settings"

                                )

                                .hasRole("ADMIN")



                                .anyRequest()

                                .authenticated()
                )

                .addFilterBefore(

                        jwtFilter,

                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
