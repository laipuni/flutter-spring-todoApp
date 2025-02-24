package project.app.flutter_spring_todoapp.member.service;

public interface MemberService {

    public void updateFcmToken(final Long memberId, final String newToken);
}
