package project.app.flutter_spring_todoapp.security.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.exception.global.LoginRequiredException;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;
import project.app.flutter_spring_todoapp.member.dto.MemberSaveDto;
import project.app.flutter_spring_todoapp.member.dto.MemberUpdateDto;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.member.service.MemberService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public Member authenticateUser(String idToken, String fcmToken) {
        //Firebase ID 토큰 검증
        FirebaseToken decodedToken = getFirebaseToken(idToken);
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();
        String profile = decodedToken.getPicture();

        //기존 유저 확인
        return memberRepository.findMemberByEmail(email)
                .map((member) -> memberService.update(MemberUpdateDto.of(member.getId(), name, email, fcmToken, profile)))
                .orElseGet(() -> memberService.save(MemberSaveDto.of(name, email, fcmToken, profile)));
    }

    private static FirebaseToken getFirebaseToken(final String idToken) {
        try {
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            log.debug("Failed google Login msg = {}, cause = {}",e.getMessage(),e.getCause());
            throw new LoginRequiredException(e.getMessage(),e.getCause());
        }
    }
}

