
package fun.gengzi.gengzi_spring_security.provider;

import fun.gengzi.gengzi_spring_security.service.impl.OtherSysOauth2LoginUserDetailsServiceImpl;
import fun.gengzi.gengzi_spring_security.sys.entity.GitHubData;
import fun.gengzi.gengzi_spring_security.token.OtherSysOauth2LoginAuthenticationToken;
import fun.gengzi.gengzi_spring_security.user.UserDetail;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

@Slf4j
public class OtherSysOauth2LoginProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserCache userCache = new NullUserCache();
    private boolean forcePrincipalAsString = false;
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private OtherSysOauth2LoginUserDetailsServiceImpl userDetailsService;


    @Override
    public void afterPropertiesSet() throws Exception {
    }


    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }


    /**
     * 认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 判断认证方式是否属于 OtherSysOauth2LoginAuthenticationToken
        Assert.isInstanceOf(OtherSysOauth2LoginAuthenticationToken.class, authentication, "仅支持OtherSysOauth2LoginAuthenticationToken类型的认证");
        boolean cacheWasUsed = true;
        // 根据信息获取 UserDetails 的信息
        UserDetail principal = (UserDetail) authentication.getPrincipal();
        String id = principal.getUsername();
        UserDetails user = this.userCache.getUserFromCache(String.valueOf(id));
        if (user == null) {
            cacheWasUsed = false;
            user = retrieveUser(String.valueOf(id),
                    (OtherSysOauth2LoginAuthenticationToken) authentication);
        }
        try {
            preAuthenticationChecks.check(user);
        } catch (AuthenticationException exception) {
            throw exception;
        }

        postAuthenticationChecks.check(user);

        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }

        Object principalToReturn = user;

        if (forcePrincipalAsString) {
            principalToReturn = user.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        OtherSysOauth2LoginAuthenticationToken result = new OtherSysOauth2LoginAuthenticationToken(
                authoritiesMapper.mapAuthorities(user.getAuthorities()),
                principal, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }

    /**
     * 检索用户
     *
     * @param username
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    protected UserDetails retrieveUser(String username, OtherSysOauth2LoginAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    public void setUserDetailsService(OtherSysOauth2LoginUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected OtherSysOauth2LoginUserDetailsServiceImpl getUserDetailsService() {
        return userDetailsService;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (OtherSysOauth2LoginAuthenticationToken.class
                .isAssignableFrom(authentication));
    }


    /**
     * 默认的预身份验证检查
     */
    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                log.debug("User account is locked");

                throw new LockedException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"));
            }

            if (!user.isEnabled()) {
                log.debug("User account is disabled");

                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                log.debug("User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"));
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("User account credentials have expired");

                throw new CredentialsExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                        "User credentials have expired"));
            }
        }
    }
}
