package fun.gengzi.gengzi_spring_security.filter;

import fun.gengzi.gengzi_spring_security.constant.Oauth2LoginRedisKeysConstant;
import fun.gengzi.gengzi_spring_security.sys.dao.OtherSysUserDao;
import fun.gengzi.gengzi_spring_security.sys.dao.SysUsersDao;
import fun.gengzi.gengzi_spring_security.sys.entity.OtherSysUser;
import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import fun.gengzi.gengzi_spring_security.sys.service.OtherUsersService;
import fun.gengzi.gengzi_spring_security.token.BindAuthenticationToken;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * <h1>用户绑定过滤器</h1>
 * <p>
 * 触发条件： 当第三方登陆用户与本系统用户绑定时，执行该过滤器
 * <p>
 * 匹配路径：/otherlogin  Post 请求
 * <p>
 * 根据绑定用户入参，将第三方用户信息入库，与本系统用户关联
 * <p>
 * 参见：UsernamePasswordAuthenticationFilter 实现
 * <p>
 *     使用示例：
 *
 *         UserBindFilter userBindFilter = new UserBindFilter();
 *         userBindFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
 *         userBindFilter.setRedisUtil(redisUtil);
 *         userBindFilter.setOtherSysUserDao(otherSysUserDao);
 *         userBindFilter.setSysUsersDao(sysUsersDao);
 *         // 再加入到 spring security 的过滤器链中
 *         httpSecurity.addFilterBefore(userBindFilter, UsernamePasswordAuthenticationFilter.class);
 *
 *
 *
 * @author gengzi
 * @date 2020年11月24日10:45:17
 */
public class UserBindFilter extends
        AbstractAuthenticationProcessingFilter {
    // ~ Static fields/initializers
    // =====================================================================================

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    public static final String SPRING_SECURITY_FORM_TOKEN_KEY = "token";
    public static final String SPRING_SECURITY_FORM_SCOPE_KEY = "scope";

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private String tokenParameter = SPRING_SECURITY_FORM_TOKEN_KEY;
    private String scopeParameter = SPRING_SECURITY_FORM_SCOPE_KEY;
    private boolean postOnly = true;

    private RedisUtil redisUtil;

    private OtherUsersService otherUsersService;

    private OtherSysUserDao otherSysUserDao;

    private SysUsersDao sysUsersDao;

    public SysUsersDao getSysUsersDao() {
        return sysUsersDao;
    }

    public void setSysUsersDao(SysUsersDao sysUsersDao) {
        this.sysUsersDao = sysUsersDao;
    }

    public OtherSysUserDao getOtherSysUserDao() {
        return otherSysUserDao;
    }

    public void setOtherSysUserDao(OtherSysUserDao otherSysUserDao) {
        this.otherSysUserDao = otherSysUserDao;
    }

    public OtherUsersService getOtherUsersService() {
        return otherUsersService;
    }

    public void setOtherUsersService(OtherUsersService otherUsersService) {
        this.otherUsersService = otherUsersService;
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
    // ~ Constructors
    // ===================================================================================================

    public UserBindFilter() {
        super(new AntPathRequestMatcher("/otherlogin", "POST"));
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String token = obtainToken(request);
        String sys = obtainScope(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        if (token == null) {
            throw new AuthenticationServiceException(
                    "token 参数缺失（The token parameter is missing）");
        }

        AuthUser authUser = (AuthUser) this.getRedisUtil().get(Oauth2LoginRedisKeysConstant.OTHER_SYS_USER_INFO + token);
        if (authUser == null) {
            throw new AuthenticationServiceException(
                    "绑定超时，请重新登陆绑定（Binding timed out, please log in again to bind）");
        }

        SysUsers sysUser = this.getSysUsersDao().findByUsername(username);

        if (sysUser == null) {
            throw new AuthenticationServiceException(
                    "输入用户名不存在（Enter username does not exist）");
        }


        // 将用户信息
        String uuid = authUser.getUuid();
        OtherSysUser otherSysUser = new OtherSysUser();
        otherSysUser.setScope(sys);
        otherSysUser.setUuid(uuid);
        otherSysUser.setCreateTime(new Date());
        otherSysUser.setUserId(sysUser.getId());
        otherSysUser.setUsername(sysUser.getUsername());
//        this.getOtherSysUserDao().save(otherSysUser);


        username = username.trim();


        BindAuthenticationToken bindAuthenticationToken = new BindAuthenticationToken(username, password,otherSysUser);

//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
//                username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, bindAuthenticationToken);
        // 移除缓存的用户数据
        this.getRedisUtil().del(Oauth2LoginRedisKeysConstant.OTHER_SYS_USER_INFO + token);
        return this.getAuthenticationManager().authenticate(bindAuthenticationToken);
    }

    // 获取密码
    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    // 获取用户名
    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    // 获取token
    @Nullable
    protected String obtainToken(HttpServletRequest request) {
        return request.getParameter(tokenParameter);
    }

    // 获取scope
    @Nullable
    protected String obtainScope(HttpServletRequest request) {
        return request.getParameter(scopeParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              BindAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return usernameParameter;
    }

    public final String getPasswordParameter() {
        return passwordParameter;
    }
}
