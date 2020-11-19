package fun.gengzi.gengzi_spring_security.config;

import fun.gengzi.gengzi_spring_security.constant.IgnoringUrlConstant;
import fun.gengzi.gengzi_spring_security.filter.ValidateCodeFilter;
import fun.gengzi.gengzi_spring_security.utils.HttpResponseUtils;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
// 启用全局的方法安全
@EnableGlobalMethodSecurity(prePostEnabled = true)
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

    @Autowired
    private ValidateCodeFilter validateCodeFilter;


    // 定义层次角色
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy();
        return roleHierarchy;
    }


    /**
     * 密码模式需要
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }


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
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // 放行swagger-ui相关的路径
                .antMatchers(IgnoringUrlConstant.IGNORING_URLS).permitAll()
                .antMatchers(IgnoringUrlConstant.IGNORING_STATIC_URLS).permitAll()
                .antMatchers(IgnoringUrlConstant.OAUTH2_URLS).permitAll()
                .antMatchers("/getLoginCode").permitAll()
                .antMatchers("/codeBuildNew/**").permitAll()  // 都可以访问
                .anyRequest().authenticated().and().formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll().and()
                .csrf().disable()// csrf 防止跨站脚本攻击
                .formLogin()
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {  // 成功登录的处理方法
                    // 成功登录所要做的操作
                    final ReturnData ret = ReturnData.newInstance();
                    ret.setSuccess();
                    ret.setMessage("登陆成功");
                    ret.setInfo(authentication);
                    HttpResponseUtils.responseResult(httpServletResponse, ret);
                }).and()
                .sessionManagement((sessionManagement) -> sessionManagement
                        .maximumSessions(100)
                        .sessionRegistry(sessionRegistry()));


    }


    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }

//
//    @Bean
//    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
//        return new HttpSessionOAuth2AuthorizationRequestRepository();
//    }

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

//    /**
//     * 认证方式：httpBasic
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // 使用 security 提供的 操作 记住我 表的 dao层
//        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        jdbcTokenRepository.setDataSource(dataSource);
//
//        // super.configure(http);
//        // http 基本认证方式  默认提供了一个简单的登陆页面
//        // http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
//
//        // 自定义表单认证方式
//        http.authorizeRequests()
//                .antMatchers("/securityBase/adminApi/**").hasRole("ADMIN")  //管理员 才能访问
//                .antMatchers("/securityBase/userApi/**").hasRole("USER")   //登录用户 才能访问
//                .antMatchers("/securityBase/publicApi/**").permitAll()  // 都可以访问
//                .anyRequest().authenticated()   // 验证所有的请求
//                .and()// and 作为中断上一个属性的配置分隔
//                .formLogin() // 表单登陆
//                .authenticationDetailsSource(myAuthenticationDetailsSource)
//                .loginPage("/login.html")// 登陆的页面
//                .loginProcessingUrl("/login")// 登陆的url
//                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {  // 成功登录的处理方法
//                    // 成功登录所要做的操作
//                    final ReturnData ret = ReturnData.newInstance();
//                    ret.setSuccess();
//                    ret.setMessage("登陆成功");
//                    ret.setInfo(authentication);
//                    HttpResponseUtils.responseResult(httpServletResponse, ret);
//                })
//                .failureHandler((httpServletRequest, httpServletResponse, e) -> {  // 认证失败的处理方法
//                    // 登陆失败所要做的操作
//                    final ReturnData ret = ReturnData.newInstance();
//                    ret.setFailure("登陆失败");
//                    HttpResponseUtils.responseResult(httpServletResponse, ret);
//
//                })
//                .permitAll()// 设置登陆页面和登陆路径 不设限访问
//                .and()
//                .csrf().disable() // csrf 防止跨站脚本攻击
//                .formLogin()
//                .and()
//                .rememberMe()// 记住我
//                .userDetailsService(userDetailService)
//                .tokenRepository(jdbcTokenRepository)
//                .and()
//                .logout()
//                .logoutSuccessUrl("/").and() // 注销登陆
//                .sessionManagement().invalidSessionStrategy(new MyInvalidSessionStrategy()) // session失效后的策略
//                .and().sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry()); // 使用 spring session 提供的会话注册表
//    }
}