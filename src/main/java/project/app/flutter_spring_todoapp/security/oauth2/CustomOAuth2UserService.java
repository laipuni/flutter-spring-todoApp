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

    public static final String SESSION_MEMBER = "member";

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();
        String attributeName = clientRegistration.getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId,
                attributeName,
                oAuth2User.getAttributes()
        );

        Member member = saveOrUpdate(attributes);

        httpSession.setAttribute(SESSION_MEMBER, SessionMember.of(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleName())),
                attributes.getAttributes(),
                attributes.getAttributesNameKey()
        );
    }

    private Member saveOrUpdate(final OAuthAttributes attributes) {
        Member member = memberRepository.findMemberByEmail(attributes.getEmail())
                .map(findMember -> findMember.update(
                        attributes.getUserName(), attributes.getEmail(), attributes.getPicture()
                ))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }
}
