package fun.gengzi.gengzi_spring_security.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;


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
        Authentication authentication = event.getAuthentication();
        // 获取主体，通常是一个用户名
        Object principal = authentication.getPrincipal();
        // 获取用户的主体信息
        Object details = authentication.getDetails();
        // 获取用户的权限列表
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 是否认证成功
        boolean authenticated = authentication.isAuthenticated();
        // 获取密码
        Object credentials = authentication.getCredentials();
        // 用户名
        String name = authentication.getName();

        log.info("info : [{}]", authentication);

    }
}

