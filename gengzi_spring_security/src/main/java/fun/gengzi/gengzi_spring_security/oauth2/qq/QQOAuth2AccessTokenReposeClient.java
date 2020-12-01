//package fun.gengzi.gengzi_spring_security.oauth2.qq;
//
//import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * 使用code 交换 accessToken
// */
//public class QQOAuth2AccessTokenReposeClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
//
//
//    private Map<String,OAuth2AccessTokenResponseClient> clients;
//
//    public Map<String, OAuth2AccessTokenResponseClient> getClients() {
//        return clients;
//    }
//
//    public QQOAuth2AccessTokenReposeClient() {
//        this.clients = new HashMap<>();
//        this.clients.put("default_key",new NimbusAuthorizationCodeTokenResponseClient());
//
//
//    }
//
//    @Override
//    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
//
//        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
//        // 获取注册id
//        String registrationId = clientRegistration.getRegistrationId();
//
//        OAuth2AccessTokenResponseClient oAuth2AccessTokenResponseClient = clients.get(registrationId);
//
//        if (oAuth2AccessTokenResponseClient == null){
//            oAuth2AccessTokenResponseClient = clients.get("default_key");
//        }
//
////        return oAuth2AccessTokenResponseClient.getTokenResponse(authorizationGrantRequest);
//
//        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
//
//        String clientId = clientRegistration.getClientId();
//        String clientSecret = clientRegistration.getClientSecret();
//
//        String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
//
//
//        String code = authorizationExchange.getAuthorizationResponse().getCode();
//        String redirectUri = authorizationExchange.getAuthorizationResponse().getRedirectUri();
//
//        // TODO 获取accessToken
//
//        // token
//        String accessToken = "";
//
//        // 响应中其他的参数
//        LinkedHashMap<String, Object> additionalParameters = new LinkedHashMap<>();
//        return OAuth2AccessTokenResponse.withToken(accessToken)
//                .tokenType(OAuth2AccessToken.TokenType.BEARER)
//                .expiresIn(600L)  // 过期时间
//                .additionalParameters(additionalParameters)
//                .build();
//    }
//}
