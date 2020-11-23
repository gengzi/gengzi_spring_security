package fun.gengzi.gengzi_spring_security.config;

import fun.gengzi.gengzi_spring_security.filter.OtherSysOauth2LoginFilter;
import fun.gengzi.gengzi_spring_security.provider.OtherSysOauth2LoginProvider;
import fun.gengzi.gengzi_spring_security.service.impl.OtherSysOauth2LoginUserDetailsServiceImpl;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 *  <h1>第三方登陆的认证配置</h1>
 *
 *  主要配置：
 *  1，加载第三方登陆的认证过滤器 ，主要提供路径拦截和基础判断
 *  设置认证管理器
 *  设置用户服务类
 *
 *  2，加载登陆提供者
 *  设置用户详细服务实现
 *  设置登陆成功事件
 *  设置登陆失败事件
 *
 *  将提供提供者和过滤器加入 HttpSecurity 中，并在 UsernamePasswordAuthenticationFilter 前执行逻辑判断
 *
 *
 */
@Configuration
public class OtherSysOauth2LoginAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private OtherSysOauth2LoginUserDetailsServiceImpl extendUserDetailsService;
//    @Autowired
//    private AuthenticationSuccessHandler successHandler;
//    @Autowired
//    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        OtherSysOauth2LoginFilter filter = new OtherSysOauth2LoginFilter();
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        filter.setUsersService(usersService);
        filter.setRedisUtil(redisUtil);
        OtherSysOauth2LoginProvider provider = new OtherSysOauth2LoginProvider();
        provider.setUserDetailsService(extendUserDetailsService);
        builder.authenticationProvider(provider).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}