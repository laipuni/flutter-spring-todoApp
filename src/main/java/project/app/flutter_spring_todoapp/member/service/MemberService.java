package project.app.flutter_spring_todoapp.member.service;

import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.dto.MemberSaveDto;
import project.app.flutter_spring_todoapp.member.dto.MemberUpdateDto;

public interface MemberService {

    public Member save(final MemberSaveDto saveDto);
    public Member update(final MemberUpdateDto updateDto);
}
