package fun.gengzi.gengzi_spring_security.sys.controller;

import cn.hutool.core.lang.UUID;
import fun.gengzi.gengzi_spring_security.constant.Oauth2LoginUrlConstant;
import fun.gengzi.gengzi_spring_security.constant.RspCodeEnum;
import fun.gengzi.gengzi_spring_security.sys.entity.GitHubData;
import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

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

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private  String clientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private  String clientSecret;
    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "github login", notes = "github login")
    @ApiImplicitParams({@ApiImplicitParam(name = "oauthSysCode", value = "oauthSysCode", required = true)})
    @GetMapping("/login")
    public String oauthLogin(@RequestParam("oauthSysCode") String oauthSysCode) {
//        boolean flag = Oauth2LoginUrlConstant.OAUTHLOGINMAP.containsKey(oauthSysCode);
//        if (!flag) {
//            return "/";
//        }
//        String url = Oauth2LoginUrlConstant.OAUTHLOGINMAP.get(oauthSysCode);
        AuthRequest authRequest = getAuthRequest();
        return "redirect:" + authRequest.authorize(AuthStateUtils.createState());
    }

    /**
     * https://zhuanlan.zhihu.com/p/144670329
     * @param callback
     * @return
     */
    @RequestMapping("/callback")
    public Object login(AuthCallback callback) {
        // 判断系统是否存在该用户信息
        // 存在，直接登陆
        // 不存在，跳转到绑定页面，要求用户绑定

        AuthRequest authRequest = getAuthRequest();
        AuthResponse<GitHubData> authResponse = authRequest.login(callback);
        if(authResponse.getCode() == Oauth2LoginUrlConstant.LOGIN_SUCCESS){
            GitHubData data = authResponse.getData();
            // 用户id
            long id = data.getRawUserInfo().getId();
            ReturnData returnData = usersService.loadUserByUsername(String.valueOf(id));
            if(returnData.getStatus() == RspCodeEnum.NOTOKEN.getCode() ){
                return "oauthlogin.html";
            }else{

            }


        }
        return "error";
    }



    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri("http://localhost:8081/api/v1/oauth/callback")  // /api/v1/oauth/callback
                .build());
    }


}
