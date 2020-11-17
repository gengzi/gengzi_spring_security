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
            "/api/v1/oauth/**",
            "/swagger-ui.html",
            "/actuator/**",
            "/v2/api-docs",
            "/webjars/**",
            "/swagger/**",
            "/swagger-resources/**",
            "/doc.html"
    };

    public static final String[] IGNORING_STATIC_URLS = {
            "/js/**"
    };

    public static final String[] OAUTH2_URLS = {
//            "/login/oauth2/code/github/**"
    };
}
