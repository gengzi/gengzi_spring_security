package fun.gengzi.gengzi_spring_security.oauth2.gitee;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 *
 * gitee  用户详情服务
 *
 * 用于构造 gitee 用户详情信息
 *
 */
public class GiteeUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientId = clientRegistration.getClientId();
        String clientSecret = clientRegistration.getClientSecret();

        String redirectUriTemplate = clientRegistration.getRedirectUriTemplate();



        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String tokenValue = accessToken.getTokenValue();

        AuthRequest authRequest = new AuthGiteeRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientId)
                .redirectUri(redirectUriTemplate)
                .build());





        return null;
    }
}
