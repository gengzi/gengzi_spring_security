package fun.gengzi.gengzi_spring_security.sys.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpRequest;
import fun.gengzi.gengzi_spring_security.constant.Oauth2LoginUrlConstant;
import fun.gengzi.gengzi_spring_security.constant.RedisKeyContants;
import fun.gengzi.gengzi_spring_security.constant.RspCodeEnum;
import fun.gengzi.gengzi_spring_security.sys.dao.SysUsersDao;
import fun.gengzi.gengzi_spring_security.sys.entity.GitHubData;
import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import fun.gengzi.gengzi_spring_security.sys.service.AuthRequestService;
import fun.gengzi.gengzi_spring_security.sys.service.UsersService;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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


    @Autowired
    private UsersService usersService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysUsersDao sysUsersDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthRequestService authRequestService;

    @ApiOperation(value = "github login", notes = "github login")
    @ApiImplicitParams({@ApiImplicitParam(name = "oauthSysCode", value = "oauthSysCode", required = true)})
    @GetMapping("/login")
    public String oauthLogin(@RequestParam("oauthSysCode") String oauthSysCode) {
//        boolean flag = Oauth2LoginUrlConstant.OAUTHLOGINMAP.containsKey(oauthSysCode);
//        if (!flag) {
//            return "/";
//        }
//        String url = Oauth2LoginUrlConstant.OAUTHLOGINMAP.get(oauthSysCode);
        AuthRequest authRequest = authRequestService.getAuthRequest(oauthSysCode);
        return "redirect:" + authRequest.authorize(AuthStateUtils.createState());
    }

//    /**
//     * https://zhuanlan.zhihu.com/p/144670329
//     *
//     * @param callback
//     * @return
//     */
//    @RequestMapping("/callback")
//    public Object login(AuthCallback callback) {
//        // 判断系统是否存在该用户信息
//        // 存在，直接登陆
//        // 不存在，跳转到绑定页面，要求用户绑定
//
//        AuthRequest authRequest = getAuthRequest();
//        AuthResponse<GitHubData> authResponse = authRequest.login(callback);
//        if (authResponse.getCode() == Oauth2LoginUrlConstant.LOGIN_SUCCESS) {
//            GitHubData data = authResponse.getData();
//            // 用户id
//            long id = data.getRawUserInfo().getId();
//            ReturnData returnData = usersService.loadUserByUsername(String.valueOf(id));
//            if (returnData.getStatus() == RspCodeEnum.NOTOKEN.getCode()) {
//                return "oauthlogin.html";
//            } else {
//
//            }
//
//
//        }
//        return "error";
//    }


    @ApiOperation(value = "bind", notes = "bind")
    @ApiImplicitParams({@ApiImplicitParam(name = "scope", value = "scope", required = true),
            @ApiImplicitParam(name = "password", value = "password", required = true)})
    @PostMapping("/bind")
    public void bind(@RequestParam("scope") String scope, @RequestParam("password") String password, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = (String) redisUtil.get("github_token:" + token);
        SysUsers sysUsers = new SysUsers();
        sysUsers.setUsername(id);
        sysUsers.setPassword(passwordEncoder.encode(password));
        sysUsers.setIsEnable(1);
        sysUsersDao.save(sysUsers);

        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        String verificationCode = captcha.getCode();
        String code = UUID.randomUUID().toString();
        // 存入redis中
        redisUtil.set(String.format(RedisKeyContants.VALIDCODEKEY, code), verificationCode, 180);

        HashMap<String, Object> map = new HashMap<>();
        map.put("username", id);
        map.put("password", password);
        map.put("uuid", code);
        map.put("validCode", verificationCode);
        String result2 = HttpRequest.post("http://localhost:8081/login")
                .form(map)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
    }


}
