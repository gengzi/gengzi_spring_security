package fun.gengzi.gengzi_spring_security.oauth2.gitee;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;

import java.util.LinkedHashMap;

public class GiteeAuth2AccessTokenReposeClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {

        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        String clientId = clientRegistration.getClientId();
        String clientSecret = clientRegistration.getClientSecret();

        OAuth2AuthorizationRequest authorizationRequest = authorizationExchange.getAuthorizationRequest();
        String redirectUri = authorizationRequest.getRedirectUri();


        AuthRequest authRequest = new AuthGiteeRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .ignoreCheckState(true)
                .build());

        // 这里已经拿到了 code 。构造一个 AuthCallback  即可。

        AuthCallback callback = getCallback(authorizationGrantRequest);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        AuthUser data = response.getData();
        String accessToken = data.getToken().getAccessToken();
        // 响应中其他的参数
        LinkedHashMap<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put("data",data);
        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(600L)  // 过期时间
                .additionalParameters(additionalParameters)
                .build();
    }


    /**
     * 从请求中构建 AuthCallback
     *
     * @return
     */
    private AuthCallback getCallback(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {

        // 授权登录后会返回code（auth_code（仅限支付宝）、authorization_code（仅限华为））

        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
        OAuth2AuthorizationResponse authorizationResponse = authorizationExchange.getAuthorizationResponse();
        String code = authorizationResponse.getCode();
        String state = authorizationResponse.getState();
        OAuth2AuthorizationRequest authorizationRequest = authorizationExchange.getAuthorizationRequest();
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        AuthCallback authCallback = AuthCallback.builder()
                .code(code)
//                .auth_code(request.getParameter("auth_code"))
//                .authorization_code(request.getParameter("authorization_code"))
//                .oauth_token(request.getParameter("oauth_token"))
                .state(state)
//                .oauth_verifier(request.getParameter("oauth_verifier"))
                .build();
        return authCallback;
    }
}
