package fun.gengzi.gengzi_spring_security.constant;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * <h1>Oauth2LoginUrl 登陆地址常量类</h1>
 * <p>
 * 可以考虑存入数据库，并加入缓存
 *
 * @author gengzi
 * @date 2020年11月17日17:00:53
 */
public class Oauth2LoginUrlConstant {


    // github oauth 授权的地址 ，参见官方文档：https://docs.github.com/en/free-pro-team@latest/developers/apps/authorizing-oauth-apps
    public static final String GITHUB_LOGIN_URL = "https://github.com/login/oauth/authorize";


    // 登陆code 和 登陆地址的映射
    public static final Map<String, String> OAUTHLOGINMAP = ImmutableMap.<String, String>builder()
            .put("GITHUB_LOGIN_URL", GITHUB_LOGIN_URL)
            .build();

}
