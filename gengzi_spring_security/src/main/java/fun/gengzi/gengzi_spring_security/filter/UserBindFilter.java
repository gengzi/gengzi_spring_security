//package fun.gengzi.gengzi_spring_security.filter;
//
//import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//@AllArgsConstructor
//@Component
//@Slf4j
//public class UserBindFilter extends OncePerRequestFilter {
//
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("token");
//        String id = (String) redisUtil.get("token:" + token);
//        // 绑定id 与 用户信息
//
//
//
//
//    }
//}
