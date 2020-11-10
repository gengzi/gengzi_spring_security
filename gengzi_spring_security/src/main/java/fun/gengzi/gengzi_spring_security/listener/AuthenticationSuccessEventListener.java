package fun.gengzi.gengzi_spring_security.listener;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


/**
 * 用户登录成功的监听事件
 *
 */
@Component
@AllArgsConstructor
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // 记录登陆日志
        Authentication authentication = event.getAuthentication();
        Object principal = authentication.getPrincipal();


    }
}