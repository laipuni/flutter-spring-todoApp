package project.app.flutter_spring_todoapp.web.resolver;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.app.flutter_spring_todoapp.exception.global.FailedResolveSessionMemberException;
import project.app.flutter_spring_todoapp.security.oauth2.dto.SessionMember;

import java.util.Optional;

import static project.app.flutter_spring_todoapp.security.oauth2.CustomOAuth2UserService.SESSION_MEMBER;

@Slf4j
public class SessionMemberArgumentResolver implements HandlerMethodArgumentResolver{

    private final HttpSession httpSession;

    public SessionMemberArgumentResolver(final HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getClass().isAssignableFrom(SessionMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        return Optional.ofNullable(httpSession.getAttribute(SESSION_MEMBER))
                .orElseThrow(() -> new FailedResolveSessionMemberException("세션 정보가 유효하지 않습니다. 다시 로그인해 주세요."));
    }
}
