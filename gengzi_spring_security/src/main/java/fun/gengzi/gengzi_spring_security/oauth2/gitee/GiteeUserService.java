package fun.gengzi.gengzi_spring_security.oauth2.gitee;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

/**
 * gitee  用户详情服务
 * <p>
 * 用于构造 gitee 用户详情信息
 */
public class GiteeUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        AuthUser authUser = null;
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientId = clientRegistration.getClientId();
        String clientSecret = clientRegistration.getClientSecret();
        String redirectUriTemplate = clientRegistration.getRedirectUriTemplate();
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String tokenValue = accessToken.getTokenValue();


        Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();

        Object data = additionalParameters.getOrDefault("data", null);

        if (data != null && data instanceof AuthUser) {
            authUser = (AuthUser) data;

            String username = authUser.getUsername();
            String uuid = authUser.getUuid();
            GiteeUserInfo giteeUserInfo = new GiteeUserInfo();
            giteeUserInfo.setPrincipal(uuid);
            giteeUserInfo.setData(additionalParameters);
            return giteeUserInfo;
        }
        return null;
    }
}
