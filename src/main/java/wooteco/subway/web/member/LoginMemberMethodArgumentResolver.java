package wooteco.subway.web.member;

import static org.springframework.web.context.request.RequestAttributes.*;
import static wooteco.subway.web.member.interceptor.BearerAuthInterceptor.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.exceptions.InvalidAuthenticationException;

@Component
public class LoginMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public LoginMemberMethodArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String)webRequest.getAttribute(LOGIN_KEY, SCOPE_REQUEST);
        if (StringUtils.isBlank(email)) {
            throw new InvalidAuthenticationException("비정상적인 로그인");
        }
        Member member = memberService.findMemberByEmail(email);
        LoginMember annotation = parameter.getParameterAnnotation(LoginMember.class);
        return annotation.type().getValidatedMember(webRequest, member);
    }
}
