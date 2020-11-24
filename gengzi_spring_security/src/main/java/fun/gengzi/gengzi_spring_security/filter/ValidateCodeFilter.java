package fun.gengzi.gengzi_spring_security.filter;

import fun.gengzi.gengzi_spring_security.constant.RedisKeyContants;
import fun.gengzi.gengzi_spring_security.constant.RspCodeEnum;
import fun.gengzi.gengzi_spring_security.exception.RrException;
import fun.gengzi.gengzi_spring_security.handler.UserAuthenticationFailureHandler;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h1>验证码过滤器</h1>
 *
 * @author gengzi
 */
@AllArgsConstructor
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    private final static String OAUTH_TOKEN_URL = "/login";
    private UserAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(OAUTH_TOKEN_URL)
                && request.getMethod().equalsIgnoreCase("POST")) {
            try {
                //校验验证码
                validate(request);
            } catch (AuthenticationException e) {
                //失败处理器
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");
        String validCode = request.getParameter("validCode");

        // 校验一下随机验证码
        String validCodeByRedis = (String) redisUtil.get(String.format(RedisKeyContants.VALIDCODEKEY, uuid));
        boolean flag = false;
        if (validCode.equals(validCodeByRedis)) {
            flag = true;
            redisUtil.del(String.format(RedisKeyContants.VALIDCODEKEY, uuid));
        }

        if (!flag) {
            throw new RrException(RspCodeEnum.ERROR_VALIDCODE.getDesc(), RspCodeEnum.ERROR_VALIDCODE.getCode());
        }
    }
}
