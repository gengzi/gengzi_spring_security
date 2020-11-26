package fun.gengzi.gengzi_spring_security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * <h1>验证码错误异常</h1>
 *
 * @author gengzi
 * @date 2020年11月26日13:49:19
 */
public class CaptchaErrorException extends AuthenticationException {
    public CaptchaErrorException(String msg) {
        super("图形验证码错误");
    }
}
