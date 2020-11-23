package fun.gengzi.gengzi_spring_security.filter;

import cn.hutool.core.lang.UUID;
import fun.gengzi.gengzi_spring_security.constant.RedisKeysConstant;
import fun.gengzi.gengzi_spring_security.constant.RspCodeEnum;
import fun.gengzi.gengzi_spring_security.sys.entity.OtherSysUser;
import fun.gengzi.gengzi_spring_security.sys.service.OtherUsersService;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import fun.gengzi.gengzi_spring_security.token.OtherSysOauth2LoginAuthenticationToken;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h1>扩展第三方登陆认证过滤器</h1>
 * <p>
 * 替代  UsernamePasswordAuthenticationFilter 过滤器功能，第三方认证的操作，都执行该过滤器操作
 * 参考:UsernamePasswordAuthenticationFilter 实现
 */
@Slf4j
//@Service
public class OtherSysOauth2LoginFilter extends AbstractAuthenticationProcessingFilter {

    // 拦截路径，触发该filter 的执行
    private static final String REDIRECTURI = "/api/v1/oauth/callback";

    // github 的认证必备参数
    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    private UsersService usersService;

    public UsersService getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    private boolean postOnly = true;


    private RedisUtil redisUtil;

    private OtherUsersService otherUsersService;

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
//        if (postOnly && !request.getMethod().equals("POST")) {
//            throw new AuthenticationServiceException(
//                    "第三方认证登陆不支持: " + request.getMethod());
//        }
        AuthRequest authRequest = this.getAuthRequest();
        AuthResponse<AuthUser> authResponse = authRequest.login(this.getCallback(request));
        if (authResponse.ok()) {
            // 获取第三方登陆信息成功
            AuthUser data = authResponse.getData();
            // 用户id
            String id = data.getUuid();

            OtherSysUser otherSysUser = this.getOtherUsersService().getOtherSysUserByUUIDAndScope("github", id);
            if(otherSysUser == null){
                String uuid = UUID.randomUUID().toString();
                redisUtil.set(RedisKeysConstant.OTHER_SYS_USER_INFO + uuid, data,300);
                // 绑定页面
                response.sendRedirect("/oauthlogin.html?token="+uuid);
                return null;
            }else {
                ReturnData returnData = this.getUsersService().loadUserByUsername(otherSysUser.getUsername());
                token = new OtherSysOauth2LoginAuthenticationToken(returnData.getInfo());
            }
        }
        this.setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("");
    }


    /**
     * 从请求中构建 AuthCallback
     *
     * @param request
     * @return
     */
    private AuthCallback getCallback(HttpServletRequest request) {
        AuthCallback authCallback = AuthCallback.builder()
                .code(request.getParameter("code"))
                .auth_code(request.getParameter("auth_code"))
                .authorization_code(request.getParameter("authorization_code"))
                .oauth_token(request.getParameter("oauth_token"))
                .state(request.getParameter("state"))
                .oauth_verifier(request.getParameter("oauth_verifier"))
                .build();
        return authCallback;
    }

    /**
     * 构造认证请求
     *
     * @return
     */
    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("e1015c0a10dc5e11b718")
                .clientSecret("72e50f683c91861107320cddfdb2948c134b3c52")
                .redirectUri("http://localhost:8081/api/v1/oauth/callback")
                .build());
    }

    protected void setDetails(HttpServletRequest request, OtherSysOauth2LoginAuthenticationToken token) {
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
