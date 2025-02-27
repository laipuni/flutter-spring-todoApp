package project.app.flutter_spring_todoapp.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.dto.MemberSaveDto;
import project.app.flutter_spring_todoapp.member.dto.MemberUpdateDto;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Member save(final MemberSaveDto saveDto) {
        Member member = memberRepository.save(saveDto.toEntity());
        log.info("새로운 사용자(id:{})를 생성했습니다.",member.getId());
        return member;
    }

    @Transactional
    @Override
    public Member update(final MemberUpdateDto updateDto) {
        Member member = memberRepository.findById(updateDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        return member.update(updateDto.getNickname(), updateDto.getEmail(), updateDto.getProfile(), updateDto.getToken());
    }
}
