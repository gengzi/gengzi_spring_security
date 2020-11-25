package fun.gengzi.gengzi_spring_security.provider;

import fun.gengzi.gengzi_spring_security.constant.RspCodeEnum;
import fun.gengzi.gengzi_spring_security.detail.CaptchaWebAuthenticationDetails;
import fun.gengzi.gengzi_spring_security.exception.RrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * 验证码认证提供者
 */
@Slf4j
@Component
public class CaptchaProvider extends DaoAuthenticationProvider {

    public CaptchaProvider(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(encoder);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 增加对验证码的校验
        // 获取验证码是否通过的标识
        CaptchaWebAuthenticationDetails details = (CaptchaWebAuthenticationDetails) authentication.getDetails();
        boolean flag = details.isFlag();
        if (!flag) {
            // 不成功 ，抛出异常
            throw new RrException(RspCodeEnum.ERROR_VALIDCODE.getDesc());
        }
        // 成功
        // 调用父类验证
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
