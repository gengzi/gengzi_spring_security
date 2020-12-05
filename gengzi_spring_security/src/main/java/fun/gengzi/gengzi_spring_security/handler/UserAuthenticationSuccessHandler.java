package fun.gengzi.gengzi_spring_security.handler;

import com.alibaba.fastjson.JSON;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * <h1>用户认证成功处理器</h1>
 * 从session中获取，响应脱敏的用户信息
 *
 * @author gengzi
 * @date 2020年11月24日13:23:40
 */
@Component
@Slf4j
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType("application/json;charset=UTF-8");
        ReturnData ret = ReturnData.newInstance();
        ret.setSuccess();
        ret.setInfo(authentication);
        response.getWriter().write(JSON.toJSONString(ret));
    }
}
