package fun.gengzi.gengzi_spring_security.handler;

import com.alibaba.fastjson.JSON;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  <h1>认证失败处理器</h1>
 */
@Component
public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @SneakyThrows
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)  {
        response.setContentType("application/json;charset=UTF-8");
        ReturnData ret = ReturnData.newInstance();
        ret.setFailure(e.getMessage());
        response.getWriter().write(JSON.toJSONString(ret));
    }
}
