package pl.fishingwear.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import pl.fishingwear.auth.LoginSuccessHandler;
import pl.fishingwear.auth.service.CustomOAuth2UserService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/rejestracja", "/logowanie", "/register", "/resetowanie-hasla", "/forgot-password", "/reset-password").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/libs/**", "/uploads/**").permitAll()
                        .requestMatchers("/lista-produktow", "/produkty/**", "/produkt/**").permitAll()
                        .requestMatchers("/login",  "/h2-console/**").permitAll()
                        .requestMatchers("/koszyk/**").permitAll()
                        .requestMatchers("/zamowienie/**").permitAll()
                        .requestMatchers("/api/v1/cart/**").permitAll()
                        .requestMatchers("/api/v1/orders/**").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/logowanie")
                        .successHandler(loginSuccessHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/logowanie")
                        .successHandler(loginSuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                )
                .logout(logout -> logout

                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/h2-console/**", "/api/v1/**")
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
//        TODO: Logout
        return http.build();
    }
}