package fun.gengzi.gengzi_spring_security.config;

import fun.gengzi.gengzi_spring_security.filter.OtherSysOauth2LoginFilter;
import fun.gengzi.gengzi_spring_security.provider.OtherSysOauth2LoginProvider;
import fun.gengzi.gengzi_spring_security.service.impl.OtherSysOauth2LoginUserDetailsServiceImpl;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class OtherSysOauth2LoginAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private OtherSysOauth2LoginUserDetailsServiceImpl extendUserDetailsService;
//    @Autowired
//    private AuthenticationSuccessHandler successHandler;
//    @Autowired
//    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private UsersService usersService;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        OtherSysOauth2LoginFilter filter = new OtherSysOauth2LoginFilter();
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        filter.setUsersService(usersService);
        OtherSysOauth2LoginProvider provider = new OtherSysOauth2LoginProvider();
        provider.setUserDetailsService(extendUserDetailsService);
        builder.authenticationProvider(provider).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}