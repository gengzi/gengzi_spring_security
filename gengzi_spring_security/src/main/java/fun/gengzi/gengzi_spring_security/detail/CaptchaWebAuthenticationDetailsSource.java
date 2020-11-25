package fun.gengzi.gengzi_spring_security.detail;


import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CaptchaWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        CaptchaWebAuthenticationDetails captchaWebAuthenticationDetails = new CaptchaWebAuthenticationDetails(context,redisUtil);
        return captchaWebAuthenticationDetails;
    }
}
