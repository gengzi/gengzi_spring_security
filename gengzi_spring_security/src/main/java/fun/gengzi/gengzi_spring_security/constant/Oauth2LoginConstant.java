package fun.gengzi.gengzi_spring_security.constant;

import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;

import java.util.HashMap;

/**
 * <h1>第三方登陆的全局静态变量</h1>
 *
 * @author gengzi
 * @date 2020年11月28日18:15:35
 */
public class Oauth2LoginConstant {

    // TODO 懒得写了，有需要增加的第三方系统，增加属性配置就可以了
    public static final String SYS_GITHUB = "github";
    public static final String SYS_GITEE = "gitee";

    // 映射的class
    public static HashMap<String, Class> sysMappingClazz = new HashMap<>();

    // 允许第三方登陆的系统
    public static final String SYS_SOURCE[] = {Oauth2LoginConstant.SYS_GITHUB, Oauth2LoginConstant.SYS_GITEE};

    static {
        sysMappingClazz.putIfAbsent(SYS_GITHUB, AuthGithubRequest.class);
        sysMappingClazz.putIfAbsent(SYS_GITEE, AuthGiteeRequest.class);
    }


}
