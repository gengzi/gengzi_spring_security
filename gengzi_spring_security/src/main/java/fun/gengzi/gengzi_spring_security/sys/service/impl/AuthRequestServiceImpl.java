package fun.gengzi.gengzi_spring_security.sys.service.impl;

import fun.gengzi.gengzi_spring_security.sys.service.AuthRequestService;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthRequestServiceImpl implements AuthRequestService {

    // github 的认证必备参数
    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String githubClientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String githubClientSecret;
    private String redirectUri;

    @Override
    public AuthRequest getAuthRequest(String sys) {
        AuthRequest authRequest = null;
        switch (sys) {
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubClientId)
                        .clientSecret(githubClientSecret)
                        .redirectUri("http://localhost:8081/api/v1/oauth/callback")
                        .build());
                break;
            case "qq":
                break;
            default:
                break;
        }
        return authRequest;
    }
}
