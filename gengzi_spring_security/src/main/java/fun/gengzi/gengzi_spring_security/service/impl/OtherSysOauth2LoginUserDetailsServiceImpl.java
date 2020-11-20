package fun.gengzi.gengzi_spring_security.service.impl;

import fun.gengzi.gengzi_spring_security.constant.RspCodeEnum;
import fun.gengzi.gengzi_spring_security.constant.UserStatusEnum;
import fun.gengzi.gengzi_spring_security.exception.RrException;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import fun.gengzi.gengzi_spring_security.user.UserDetail;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * <h1>用户详细服务impl</h1>
 * <p>
 * 用于返回根据用户名返回用户详细信息，以便于供 security 使用
 *
 * @author gengzi
 * @date 2020年11月3日15:24:43
 */
@Service("otherSysOauth2LoginUserDetailsServiceImpl")
public class OtherSysOauth2LoginUserDetailsServiceImpl {

    @Autowired
    private UsersService usersService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ReturnData result = usersService.loadUserByUsername(username);
        if (RspCodeEnum.NOTOKEN.getCode() == result.getStatus()) {
            throw new RrException(RspCodeEnum.ACCOUNT_NOT_EXIST.getDesc());
        }
        UserDetail userDetail = (UserDetail) result.getInfo();
        if (userDetail == null) {
            throw new RrException(RspCodeEnum.ACCOUNT_NOT_EXIST.getDesc());
        }
        //账号不可用
        if (userDetail.getStatus() == UserStatusEnum.DISABLE.getValue()) {
            userDetail.setEnabled(false);
        }
        return userDetail;
    }
}
