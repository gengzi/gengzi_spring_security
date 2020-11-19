/**
 *
 *  暂时弃用
 *
 *
 */


//package fun.gengzi.gengzi_spring_security.oauth2;
//
//import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <h1>自定义认证请求存储</h1>
// * <p>
// * 原有的默认实现，通过 httpSession 来保存和响应，在前后端分离后，无法使用cookie 和 session 的形式。
// * 现有实现，通过redis 来保存和响应。通过 请求头中的 token 和 redis session 的形式
// */
//@Component
//public final class HeardTokenOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
//    private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME =
//            HeardTokenOAuth2AuthorizationRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";
//
//    private final String sessionAttributeName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME;
//
//    private static final String HEADER_AUTHENTICATION_INFO = "Authentication-Info";
//    private static final String REDIS_KEY = "token:";
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Override
//    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
//        Assert.notNull(request, "request cannot be null");
//        String stateParameter = this.getStateParameter(request);
//        if (stateParameter == null) {
//            return null;
//        }
//        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
//        return authorizationRequests.get(stateParameter);
//    }
//
//    @Override
//    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
//                                         HttpServletResponse response) {
//        Assert.notNull(request, "request cannot be null");
//        Assert.notNull(response, "response cannot be null");
//        if (authorizationRequest == null) {
//            this.removeAuthorizationRequest(request, response);
//            return;
//        }
//        String state = authorizationRequest.getState();
//        Assert.hasText(state, "authorizationRequest.state cannot be empty");
//        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
//        authorizationRequests.put(state, authorizationRequest);
//        String token = request.getHeader(HEADER_AUTHENTICATION_INFO);
//        redisUtil.set(REDIS_KEY + token, authorizationRequests);
////		request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);
//    }
//
//    @Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
//        Assert.notNull(request, "request cannot be null");
//        String stateParameter = this.getStateParameter(request);
//        if (stateParameter == null) {
//            return null;
//        }
//        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
//        OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);
//        if (!authorizationRequests.isEmpty()) {
////			redisUtil.
////
////
////			request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);
//        } else {
////			request.getSession().removeAttribute(this.sessionAttributeName);
//        }
//        return originalRequest;
//    }
//
//    @Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
//        Assert.notNull(response, "response cannot be null");
//        return this.removeAuthorizationRequest(request);
//    }
//
//    /**
//     * Gets the state parameter from the {@link HttpServletRequest}
//     *
//     * @param request the request to use
//     * @return the state parameter or null if not found
//     */
//    private String getStateParameter(HttpServletRequest request) {
//        return request.getParameter(OAuth2ParameterNames.STATE);
//    }
//
//    private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
//        String token = request.getHeader(HEADER_AUTHENTICATION_INFO);
//        Map<String, OAuth2AuthorizationRequest> authorizationRequests = (Map<String, OAuth2AuthorizationRequest>) redisUtil.get(REDIS_KEY + token);
//        if (authorizationRequests == null) {
//            return new HashMap<>();
//        }
//        return authorizationRequests;
//    }
//}
