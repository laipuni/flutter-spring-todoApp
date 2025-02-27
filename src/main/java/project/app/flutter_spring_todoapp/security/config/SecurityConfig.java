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
                .authorizeHttpRequests(authentication -> {
                    authentication.requestMatchers("/api/auth/**").permitAll() // 로그인 관련 url은 허용
                            .anyRequest().authenticated(); // 나머지 url은 인증이 필요 함
                })
                .addFilterBefore(firebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public Filter firebaseAuthenticationFilter(){
        return new FirebaseAuthenticationFilter(firebaseAuth, memberRepository);
    }
}
