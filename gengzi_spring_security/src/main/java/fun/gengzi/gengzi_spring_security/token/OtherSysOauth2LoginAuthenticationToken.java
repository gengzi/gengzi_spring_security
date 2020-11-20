package fun.gengzi.gengzi_spring_security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class OtherSysOauth2LoginAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;


    public OtherSysOauth2LoginAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public OtherSysOauth2LoginAuthenticationToken(Object authUser) {
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
    public OtherSysOauth2LoginAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override

    }

//    @Override
//    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//        if (isAuthenticated) {
//            throw new IllegalArgumentException(
//                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
//        }
//        super.setAuthenticated(false);
//    }



    // 密码
    @Override
    public Object getCredentials() {
        return this.credentials;
    }


    // 用户名
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
