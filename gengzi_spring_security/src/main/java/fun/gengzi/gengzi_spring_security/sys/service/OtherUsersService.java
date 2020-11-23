package fun.gengzi.gengzi_spring_security.sys.service;


import fun.gengzi.gengzi_spring_security.sys.entity.OtherSysUser;

/**
 * <h1>第三方系统用户服务</h1>
 *
 * @author gengzi
 * @date 2020年11月23日21:10:59
 */
public interface OtherUsersService {


    /**
     * 根据 id 获取用户信息
     *
     * @param uuid  第三方系统的id
     * @param scope 那个第三方系统
     * @return
     */
    OtherSysUser getOtherSysUserByUUIDAndScope(String scope, String uuid);


}
