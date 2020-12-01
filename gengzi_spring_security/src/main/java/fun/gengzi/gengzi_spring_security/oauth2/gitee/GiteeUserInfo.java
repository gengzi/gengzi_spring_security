package fun.gengzi.gengzi_spring_security.oauth2.gitee;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * gitee 用户详情
 *
 */
public class GiteeUserInfo implements OAuth2User {

    // 一个主体，通常是一个用户名
    private String principal;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }


    /**
     * Get the OAuth 2.0 token attributes
     * 获取oauth2 的令牌属性
     *
     * @return the OAuth 2.0 token attributes
     */
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    /**
     * Get the {@link Collection} of {@link GrantedAuthority}s associated
     * with this OAuth 2.0 token
     *
     * 获取权限集合，新用户登陆可赋值一个默认的权限
     *
     * @return the OAuth 2.0 token authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 默认的权限集合
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("NOMOR_USER");
        grantedAuthorities.add(simpleGrantedAuthority);
        return grantedAuthorities;
    }

    /**
     * Returns the name of the authenticated <code>Principal</code>. Never <code>null</code>.
     *
     * // 返回认证的 Principal
     *
     *
     * @return the name of the authenticated <code>Principal</code>
     */
    @Override
    public String getName() {
        return this.principal;
    }
}
