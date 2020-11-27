package fun.gengzi.gengzi_spring_security.sys.service.impl;

import fun.gengzi.gengzi_spring_security.sys.service.AuthRequestService;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthRequestServiceImpl implements AuthRequestService {

    // github 的认证必备参数
    @Value("${oauth2.github.client-id}")
    private String githubClientId;
    @Value("${oauth2.github.client-secret}")
    private String githubClientSecret;
    @Value("${oauth2.github.redirectUri}")
    private String githubRedirectUri;

    @Value("${oauth2.gitee.client-id}")
    private String giteeClientId;
    @Value("${oauth2.gitee.client-secret}")
    private String giteeClientSecret;
    @Value("${oauth2.gitee.redirectUri}")
    private String giteeRedirectUri;


    @Override
    public AuthRequest getAuthRequest(String sys) {
        AuthRequest authRequest = null;
        switch (sys) {
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubClientId)
                        .clientSecret(githubClientSecret)
                        .redirectUri(githubRedirectUri)
                        .build());
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteeClientId)
                        .clientSecret(giteeClientSecret)
                        .redirectUri(giteeRedirectUri)
                        .build());
                break;
            default:
                authRequest = null;
                break;
        }
        return authRequest;
    }
}
