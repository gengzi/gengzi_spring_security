package fun.gengzi.gengzi_spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * <h1>security 配置</h1>
 * <p>
 * 401 用户认证失败
 * 403 用户授权失败
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

    /**
     * 认证方式：httpBasic
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 自定义表单认证方式
        http.authorizeRequests()
                .anyRequest().authenticated().and().formLogin().and().httpBasic().and()// and 作为中断上一个属性的配置分隔
                .csrf().disable()// csrf 防止跨站脚本攻击
                .sessionManagement((sessionManagement) -> sessionManagement
                        .maximumSessions(2)
                        .sessionRegistry(sessionRegistry()));
    }



}