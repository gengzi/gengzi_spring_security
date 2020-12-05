package fun.gengzi.gengzi_spring_security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <h1>绑定登陆认证令牌</h1>
 * <p>
 * 参考： UsernamePasswordAuthenticationToken 实现
 * <p>
 * 将登录信息构造一个成认证令牌，传递数据
 *
 * @author gengzi
 * @date 2020年12月5日10:49:24
 */
public class BindAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;
    // 绑定信息
    private Object bindInfo;

    public Object getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(Object bindInfo) {
        this.bindInfo = bindInfo;
    }

    public BindAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public BindAuthenticationToken(Object principal, Object credentials, Object bindInfo) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.bindInfo = bindInfo;
        setAuthenticated(false);
    }

    public BindAuthenticationToken(Object authUser) {
        super(null);
        this.principal = authUser;
        setAuthenticated(false);
    }

    /**
     * 使用提供的权限数组创建令牌。
     *
     * @param authorities 权限集合
     * @param principal   用户名
     * @param credentials 密码
     */
    public BindAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override

    }

    // 密码
    @Override
    public Object getCredentials() {
        return this.credentials;
    }


    // 用户名 或者 主体
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
