package fun.gengzi.gengzi_spring_security.config;

import fun.gengzi.gengzi_spring_security.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * <h1>security 配置</h1>
 * <p>
 * 401 用户认证失败
 * 403 用户授权失败
 *
 * @author gengzi
 * @date 2020年11月3日14:56:06
 */
@Configuration
// 启用web 认证
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    // ------------  用户详细服务 -------------
    @Autowired
    private UserDetailsService userDetailsService;

    // ------------  密码加密策略 -------------
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ------------  会话相关配置 start -------------
    // 会话存储库
    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    // spring session 会话注册表
    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }
    // ------------  会话相关配置 end -------------


    /**
     * 安全过滤器链配置方法
     * 针对http请求的安全配置
     * <p>
     * 1,针对请求路径的处理，是否需要认证后访问，是否需要权限访问
     * 2,配置登陆路径，登陆页面路径
     * 3,配置认证成功事件，认证失败事件
     * 4,配置防止csrf攻击
     * 5,配置自动登录配置（记住我）
     * 6,配置注销登陆相关路径，注销成功响应
     * 7,配置会话信息管理
     * 8,配置session失效后的策略
     * 9,配置认证过程中的filter 比如验证码过滤器
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

    /**
     * 认证管理器配置方法
     * <p>
     * 用户身份的管理者（AuthenticationManager），认证的入口
     * 比如需要使用 userDetailsService 的用户信息，设置 auth.userDetailsService(userDetailsService)
     * 比如需要使用 内存用户信息，设置 auth.inMemoryAuthentication()
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 核心过滤器配置方法
     * <p>
     * 一般用来忽略security 对静态资源的控制
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS);
    }
}