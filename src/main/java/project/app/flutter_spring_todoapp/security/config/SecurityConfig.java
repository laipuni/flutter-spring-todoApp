package project.app.flutter_spring_todoapp.security.config;

import com.google.firebase.auth.FirebaseAuth;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.security.filter.FirebaseAuthenticationFilter;
import project.app.flutter_spring_todoapp.security.oauth2.CustomOAuth2UserService;

@Configuration
public class SecurityConfig {

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FirebaseAuth firebaseAuth;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth)->oauth
                        .loginPage("/")
                        .userInfoEndpoint((endPoint)->endPoint
                                .userService(customOAuth2UserService) // OAuth2유저의 정보의 EndPoint
                        )
                        .defaultSuccessUrl("/")// 로그인이 성공했을 때 redirect url
                )
                .addFilterBefore(firebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public Filter firebaseAuthenticationFilter(){
        return new FirebaseAuthenticationFilter(firebaseAuth, memberRepository);
    }
}
