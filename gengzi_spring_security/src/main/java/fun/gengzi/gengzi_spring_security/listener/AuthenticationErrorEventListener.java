package fun.gengzi.gengzi_spring_security.listener;

import cn.hutool.http.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


/**
 * 用户认证失败的监听事件
 */
@Component
@AllArgsConstructor
@Slf4j
public class AuthenticationErrorEventListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    /**
     * @param event 认证失败事件
     */
    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        // 如果一个用户认证失败多次，就控制该用户被锁定，等待管理员解锁
        log.info(event.toString());



    }
}