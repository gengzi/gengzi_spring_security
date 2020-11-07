package fun.gengzi.gengzi_spring_security.constant;


/**
 * <h1>忽略的url路径</h1>
 *
 * @author gengzi
 * @date 2020年11月7日12:10:09
 */
public class IgnoringUrlConstant {
    /**
     * 不进行认证的URL
     */
    public static final String[] IGNORING_URLS = {
            "/swagger-ui.html",
            "/actuator/**",
            "/v2/api-docs",
            "/webjars/**",
            "/swagger/**",
            "/swagger-resources/**",
            "/doc.html"
    };

}
