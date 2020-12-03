package fun.gengzi.gengzi_spring_security.sys.controller;

import fun.gengzi.gengzi_spring_security.sys.service.AuthRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>oauth controller 第三方登陆controller</h1>
 *
 * @author gengzi
 * @date 2020年11月17日16:55:28
 */
@Api(value = "第三方登陆", tags = {"第三方登陆"})
@Controller
@RequestMapping("/api/v1/oauth")
public class Oauth2LoginController {

    @Autowired
    private AuthRequestService authRequestService;

    @ApiOperation(value = "登陆接口", notes = "登陆接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "oauthSysCode", value = "第三方系统syscode", required = true)})
    @GetMapping("/login")
    public String oauthLogin(@RequestParam("oauthSysCode") String oauthSysCode) {
        // 根据code ，获取对应第三方系统的 AuthRequest
        AuthRequest authRequest = authRequestService.getAuthRequest(oauthSysCode);
        // 重定向认证地址
        return "redirect:" + authRequest.authorize(AuthStateUtils.createState());
    }

}
