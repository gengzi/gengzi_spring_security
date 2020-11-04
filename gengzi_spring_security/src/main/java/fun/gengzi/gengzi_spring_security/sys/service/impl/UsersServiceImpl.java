package fun.gengzi.gengzi_spring_security.sys.service.impl;

import fun.gengzi.gengzi_spring_security.sys.dao.SysUsersDao;
import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import fun.gengzi.gengzi_spring_security.user.UserDetail;
import fun.gengzi.gengzi_spring_security.utils.ConvertUtils;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private SysUsersDao sysUsersDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ReturnData loadUserByUsername(String userName) {
        ReturnData ret = ReturnData.newInstance();
        SysUsers sysUsers = sysUsersDao.findByUsername(userName);
        UserDetail userDetail = ConvertUtils.sourceToTarget(sysUsers, UserDetail.class);
        userDetail.setStatus(sysUsers.getIsEnable());
//        userDetail.setPassword(passwordEncoder.encode(userDetail.getPassword()));
        Set<String> permsSet = new HashSet<>();
        permsSet.add("admin");
        //封装权限标识
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(permsSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        userDetail.setAuthorities(authorities);
        ret.setSuccess();
        ret.setInfo(userDetail);
        return ret;
    }
}
