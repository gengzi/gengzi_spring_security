package fun.gengzi.gengzi_spring_security.sys.service;

import me.zhyd.oauth.request.AuthRequest;

/**
 * <h1>提供验证请求服务</h1>
 *
 * @author gengzi
 * @date 2020年11月27日16:58:03
 */
public interface AuthRequestService {

    /**
     * 根据系统来源，返回不同的第三方认证请求
     *
     * @param sys 系统来源 （github，gitee）
     * @return
     */
    AuthRequest getAuthRequest(String sys);
}
