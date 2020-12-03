package fun.gengzi.gengzi_spring_security.filter;

import cn.hutool.core.lang.UUID;
import fun.gengzi.gengzi_spring_security.constant.Oauth2LoginConstant;
import fun.gengzi.gengzi_spring_security.constant.Oauth2LoginRedisKeysConstant;
import fun.gengzi.gengzi_spring_security.sys.entity.OtherSysUser;
import fun.gengzi.gengzi_spring_security.sys.service.AuthRequestService;
import fun.gengzi.gengzi_spring_security.sys.service.OtherUsersService;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import fun.gengzi.gengzi_spring_security.token.OtherSysOauth2LoginAuthenticationToken;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * <h1>扩展第三方登陆认证过滤器</h1>
 * <p>
 * 第三方认证的操作，都执行该过滤器操作
 * 参考:UsernamePasswordAuthenticationFilter 实现
 */
@Slf4j
public class OtherSysOauth2LoginFilter extends AbstractAuthenticationProcessingFilter {

    // 拦截路径，触发该filter 的执行
    private static final String REDIRECTURI = "/api/v1/oauth/callback/**";

    // 用户服务
    private UsersService usersService;

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    private boolean postOnly = true;

    // redis 工具类
    private RedisUtil redisUtil;
    // 第三方用户服务
    private OtherUsersService otherUsersService;
    // 认证请求服务
    private AuthRequestService authRequestService;

    public AuthRequestService getAuthRequestService() {
        return authRequestService;
    }

    public void setAuthRequestService(AuthRequestService authRequestService) {
        this.authRequestService = authRequestService;
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

    /**
     * 初始化拦截路径
     */
    public OtherSysOauth2LoginFilter() {
        super(new AntPathRequestMatcher(REDIRECTURI));
    }

    /**
     * 真实的身份认证-扩展第三方登陆的认证
     * 成功认证，返回认证令牌
     * 认证失败，抛出AuthenticationException
     * <p>
     * // 根据request 构造AuthCallback
     * // 根据AuthCallback  执行第三方登陆操作
     * // 登陆后获取用户信息
     * // 根据用户信息，判断当前用户是否在系统中已经绑定，
     * // 绑定，执行登陆
     * // 没有绑定，返回绑定页面
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        OtherSysOauth2LoginAuthenticationToken token = null;
        // 解析地址，获得第三方平台syscode
        String path = request.getServletPath();
        String[] sysArr = path.split("/api/v1/oauth/callback/");
        String sys = sysArr[sysArr.length - 1];
        // 判断当前系统是否支持该平台登陆
        boolean contains = Arrays.asList(Oauth2LoginConstant.SYS_SOURCE).contains(sys);
        if (!contains) {
            throw new AuthenticationServiceException(
                    "暂不支持此系统登录（This system login is not currently supported）");
        }
        // 构造AuthRequest
        AuthRequest authRequest = this.getAuthRequest(sys);
        if (authRequest == null) {
            throw new AuthenticationServiceException(
                    "暂不支持此系统登录（This system login is not currently supported）");
        }
        // 去第三方平台获取用户信息
        AuthResponse<AuthUser> authResponse = authRequest.login(this.getCallback(request));
        if (authResponse.ok()) {
            // 获取第三方登陆信息成功
            AuthUser data = authResponse.getData();
            // 用户id 一般是唯一的。建议通过uuid + source的方式唯一确定一个用户，这样可以解决用户身份归属的问题。
            String id = data.getUuid();
            // 根据当前系统和uuid，查询数据库，获取绑定本系统的用户信息
            OtherSysUser otherSysUser = this.getOtherUsersService().getOtherSysUserByUUIDAndScope(sys, id);
            if (otherSysUser == null) {
                // 未绑定过，第一次使用这个第三方平台登陆
                String uuid = UUID.randomUUID().toString();
                // 缓存一下第三方平台的用户信息，方便后续使用
                redisUtil.set(Oauth2LoginRedisKeysConstant.OTHER_SYS_USER_INFO + uuid, data, 300);
                // 跳转到绑定页面
                response.sendRedirect("/oauthlogin.html?token=" + uuid + "&scope=" + sys);
                return null;
            } else {
                // 存在绑定用户信息，说明不是第一次使用这个第三方平台登陆，执行认证流程
                ReturnData returnData = this.getUsersService().loadUserByUsername(otherSysUser.getUsername());
                token = new OtherSysOauth2LoginAuthenticationToken(returnData.getInfo());
            }
        }
        // 设置额外参数
        this.setDetails(request, token);
        // 去认证
        return this.getAuthenticationManager().authenticate(token);
    }


    /**
     * 从请求中构建 AuthCallback
     *
     * @param request 回调请求
     * @return
     */
    private AuthCallback getCallback(HttpServletRequest request) {
        AuthCallback authCallback = AuthCallback.builder()
                .code(request.getParameter("code"))  // code 重要
                .auth_code(request.getParameter("auth_code"))
                .authorization_code(request.getParameter("authorization_code"))
                .oauth_token(request.getParameter("oauth_token"))
                .state(request.getParameter("state")) // state 重要
                .oauth_verifier(request.getParameter("oauth_verifier"))
                .build();
        return authCallback;
    }

    /**
     * 构造认证请求
     *
     * @param sys 系统来源
     * @return
     */
    private AuthRequest getAuthRequest(String sys) {
        return authRequestService.getAuthRequest(sys);
    }

    /**
     * 设置额外参数
     *
     * @param request
     * @param token
     */
    protected void setDetails(HttpServletRequest request, OtherSysOauth2LoginAuthenticationToken token) {
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
