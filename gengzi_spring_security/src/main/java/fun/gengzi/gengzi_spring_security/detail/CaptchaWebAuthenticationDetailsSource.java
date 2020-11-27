package fun.gengzi.gengzi_spring_security.detail;


import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * <H1>验证码Web身份验证详细信息源</H1>
 * <p>
 * 用于替换 UsernamePasswordAuthenticationFilter 中默认的  AuthenticationDetailsSource 属性
 * 此类需要在核心配置中配置
 * 代码示例：
 * <p>
 * protected void configure(HttpSecurity http) throws Exception {
 *    http...
 *        .formLogin()
 *        .authenticationDetailsSource(captchaWebAuthenticationDetailsSource)  // 替换原有的authenticationDetailsSource
 *        ....
 *        ;
 * }
 *
 * @author gengzi
 * @date 2020年11月27日15:43:06
 */
@Component
public class CaptchaWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        CaptchaWebAuthenticationDetails captchaWebAuthenticationDetails = new CaptchaWebAuthenticationDetails(context, redisUtil);
        return captchaWebAuthenticationDetails;
    }
}
