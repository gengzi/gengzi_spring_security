package fun.gengzi.gengzi_spring_security.sys.service.impl;

import fun.gengzi.gengzi_spring_security.sys.dao.OtherSysUserDao;
import fun.gengzi.gengzi_spring_security.sys.entity.OtherSysUser;
import fun.gengzi.gengzi_spring_security.sys.service.OtherUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OtherUserServiceImpl implements OtherUsersService {


    @Autowired
    private OtherSysUserDao otherSysUserDao;
    /**
     * 根据 id 获取用户信息
     *
     * @param scope 那个第三方系统
     * @param uuid  第三方系统的id
     * @return
     */
    @Override
    public OtherSysUser getOtherSysUserByUUIDAndScope(String scope, String uuid) {
        log.info("获取第三方系统用户入参：[scope:{},uuid:{}]", scope, uuid);
        OtherSysUser otherSysUser = otherSysUserDao.findByUuidAndScope(uuid,scope);
        log.info("获取第三方系统用户出参：[OtherSysUser:{}]", otherSysUser);
        return otherSysUser;
    }


}
