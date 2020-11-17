package fun.gengzi.gengzi_spring_security.sys.controller;

import cn.hutool.core.lang.UUID;
import fun.gengzi.gengzi_spring_security.constant.Oauth2LoginUrlConstant;
import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <h1>oauth controller</h1>
 *
 * @author gengzi
 * @date 2020年11月17日16:55:28
 */
@Api(value = "Oauth2LoginController", tags = {"Oauth2LoginController"})
@Controller
@RequestMapping("/api/v1/oauth")
public class Oauth2LoginController {


    @ApiOperation(value = "github login", notes = "github login")
    @ApiImplicitParams({@ApiImplicitParam(name = "oauthSysCode", value = "oauthSysCode", required = true)})
    @GetMapping("/login")
    public String oauthLogin(@RequestParam("oauthSysCode") String oauthSysCode) {
        boolean flag = Oauth2LoginUrlConstant.OAUTHLOGINMAP.containsKey(oauthSysCode);
        if (!flag) {
            return "/";
        }
        String url = Oauth2LoginUrlConstant.OAUTHLOGINMAP.get(oauthSysCode);
        // 拼接参数
//        client_id
        String state = UUID.randomUUID().toString();


        return "redirect:" + url;
    }


}
