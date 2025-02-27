package project.app.flutter_spring_todoapp.security.oauth2;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.security.oauth2.dto.OAuthAttributes;
import project.app.flutter_spring_todoapp.security.oauth2.dto.SessionMember;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {


}
