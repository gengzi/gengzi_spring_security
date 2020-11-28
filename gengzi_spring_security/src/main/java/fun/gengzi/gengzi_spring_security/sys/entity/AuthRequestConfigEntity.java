package fun.gengzi.gengzi_spring_security.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>身份验证请求配置实体</h1>
 * <p>
 * 读取yml配置，构造对象
 *
 * @author gengzi
 * @date 2020年11月28日17:30:24
 */
@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestConfigEntity<T> implements Serializable {

    // 所有的第三方登陆的客户端信息
    private List<AuthRequestInfo> othersys = new ArrayList<>();

    /**
     * 具体信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthRequestInfo {
        // 第三方系统名称
        private String name;
        // 客户端id
        private String clientId;
        // 客户端密钥
        private String clientSecret;
        // 回调地址
        private String redirectUri;
    }

}
