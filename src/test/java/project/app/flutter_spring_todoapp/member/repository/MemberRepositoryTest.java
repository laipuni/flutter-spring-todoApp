package project.app.flutter_spring_todoapp.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("이메일로 유저를 조회한다.")
    @Test
    void findMemberByEmail(){
        //given
        String email = "email@email.com";
        String nickName = "닉네임";
        String profile = "profile";
        Member member = Member.builder()
                .nickName(nickName)
                .role(Role.USER)
                .email(email)
                .profile(profile)
                .build();

        memberRepository.save(member);

        //when
        Member result = memberRepository.findMemberByEmail(email).get();

        //then
        assertThat(result).isNotNull()
                .extracting("nickName","profile","email")
                .containsExactly(nickName,profile,email);
    }

}