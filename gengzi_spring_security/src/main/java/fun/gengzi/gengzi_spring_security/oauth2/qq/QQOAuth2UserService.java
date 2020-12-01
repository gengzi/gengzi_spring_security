//package fun.gengzi.gengzi_spring_security.oauth2.qq;
//
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//public class QQOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    // 获取用户信息，构造用户信息
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2AccessToken accessToken = userRequest.getAccessToken();
//        String tokenValue = accessToken.getTokenValue();
//
//
//
//        return null;
//    }
//}
