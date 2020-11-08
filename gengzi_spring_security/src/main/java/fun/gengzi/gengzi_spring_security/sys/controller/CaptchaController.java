package fun.gengzi.gengzi_spring_security.sys.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import fun.gengzi.gengzi_spring_security.constant.RedisKeyContants;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import fun.gengzi.gengzi_spring_security.vo.ReturnData;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <h1>提供图片验证码ctl</h1>
 *
 * @author gengzi
 * @date 2020年11月8日12:32:54
 */
@Controller
@AllArgsConstructor
@Api(tags = "验证码")
public class CaptchaController {

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "获取登陆随机验证码", notes = "获取登陆随机验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "code", value = "code", required = true)})
    @GetMapping("/getLoginCode")
    @ResponseBody
    public String getLoginCode(@RequestParam("code") String code, HttpServletResponse response) {
        ReturnData ret = ReturnData.newInstance();
        if (StringUtils.isBlank(code)) {
            return "error";
        }
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        String verificationCode = captcha.getCode();
        // 存入redis中
        redisUtil.set(String.format(RedisKeyContants.VALIDCODEKEY, code), verificationCode, 180);
        //图形验证码写出，可以写出到文件，也可以写出到流
        return "data:image/png;base64," + captcha.getImageBase64();
    }

}
