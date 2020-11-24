package fun.gengzi.gengzi_spring_security.sys.service;

import me.zhyd.oauth.request.AuthRequest;

/**
 * <h1>提供验证请求服务</h1>
 */
public interface AuthRequestService {

    AuthRequest getAuthRequest(String sys);
}
