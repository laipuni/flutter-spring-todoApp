package project.app.flutter_spring_todoapp.security.filter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import project.app.flutter_spring_todoapp.exception.fcm.FailedFirebaseInitException;
import project.app.flutter_spring_todoapp.exception.global.LoginRequiredException;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthenticationFilter(FirebaseAuth firebaseAuth, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String idToken = header.replace("Bearer ", "");
        FirebaseToken decodedToken = getFirebaseToken(response, idToken);
        memberRepository.findMemberByEmail(decodedToken.getEmail())
                .ifPresent(FirebaseAuthenticationFilter::setAuthentication);
        chain.doFilter(request, response);
    }

    private FirebaseToken getFirebaseToken(final HttpServletResponse response, final String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            throw new LoginRequiredException(e.getMessage(),e.getCause());
        }
    }

    private static void setAuthentication(final Member member) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRoleName()) // 사용자의 권한을 Holder에 저장하기 위해 생성
        );
        //SecurityContext에 사용자 정보 저장
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(member.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
