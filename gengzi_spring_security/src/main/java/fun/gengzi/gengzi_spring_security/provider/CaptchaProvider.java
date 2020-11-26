package fun.gengzi.gengzi_spring_security.provider;

import fun.gengzi.gengzi_spring_security.constant.RspCodeEnum;
import fun.gengzi.gengzi_spring_security.detail.CaptchaWebAuthenticationDetails;
import fun.gengzi.gengzi_spring_security.exception.CaptchaErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * <h1>验证码认证提供者</h1>
 *
 * @author gengzi
 * @date 2020年11月26日13:51:32
 */
@Slf4j
@Component
public class CaptchaProvider extends DaoAuthenticationProvider {

    /**
     * 使用构造方法，将 userDetailsService 和 encoder 注入
     *
     * @param userDetailsService 用户详细服务
     * @param encoder            密码加密方式
     */
    public CaptchaProvider(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(encoder);
    }

    /**
     * 验证码验证是否正确
     * @param userDetails
     * @param authentication
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 获取验证码是否通过的标识
        CaptchaWebAuthenticationDetails details = (CaptchaWebAuthenticationDetails) authentication.getDetails();
        boolean flag = details.isFlag();
        if (!flag) {
            // 不成功 ，抛出异常
            throw new CaptchaErrorException(RspCodeEnum.ERROR_VALIDCODE.getDesc());
        }
        // 成功，调用父类验证
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
