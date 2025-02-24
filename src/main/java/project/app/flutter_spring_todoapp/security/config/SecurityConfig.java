package project.app.flutter_spring_todoapp.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import project.app.flutter_spring_todoapp.security.oauth2.CustomOAuth2UserService;

@Configuration
public class SecurityConfig {

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth)->oauth
                        .loginPage("/")
                        .userInfoEndpoint((endPoint)->endPoint
                                .userService(customOAuth2UserService) // OAuth2유저의 정보의 EndPoint
                        )
                        .defaultSuccessUrl("/")// 로그인이 성공했을 때 redirect url
                )
                .build();
    }
}
