package fun.gengzi.gengzi_spring_security.sys.controller;


import fun.gengzi.gengzi_spring_security.sys.dao.SysUsersDao;
import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "UsersController", tags = {"UsersController"})
@Controller
@RequestMapping("/api/v1/user")
public class UsersController {

    @Autowired
    private SysUsersDao sysUsersDao;


    @ApiOperation(value = "根据用户名获取用户信息", notes = "根据用户名获取用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "username", required = true)})
//    @PreAuthorize("hasRole('admin')")
    @PreAuthorize("hasAuthority('sys:manager:qry')")
    @GetMapping("/infoByUserName")
    @ResponseBody
    public ReturnData getMyPrizeInfo(@RequestParam("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReturnData ret = ReturnData.newInstance();
        SysUsers sysUsers = sysUsersDao.findByUsername(username);
        ret.setSuccess();
        ret.setInfo(sysUsers);
        ret.setMessage(authentication);
        return ret;
    }


}
