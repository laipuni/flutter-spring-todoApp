package project.app.flutter_spring_todoapp.web.resolver;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.app.flutter_spring_todoapp.exception.global.FailedResolveSessionMemberException;
import project.app.flutter_spring_todoapp.exception.global.LoginRequiredException;
import project.app.flutter_spring_todoapp.security.oauth2.dto.SessionMember;

import java.util.Optional;

@Slf4j
public class SessionMemberArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SessionMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SessionMember sessionMember) {
            return sessionMember;
        }

        log.trace("로그인 정보가 없어 SessionMember 매핑에 실패");
        throw new LoginRequiredException("로그인이 필요합니다.");
    }
}
