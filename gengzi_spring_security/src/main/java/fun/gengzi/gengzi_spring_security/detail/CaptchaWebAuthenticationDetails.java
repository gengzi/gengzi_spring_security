package fun.gengzi.gengzi_spring_security.detail;

import fun.gengzi.gengzi_spring_security.constant.RedisKeyContants;
import fun.gengzi.gengzi_spring_security.utils.RedisUtil;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CaptchaWebAuthenticationDetails extends WebAuthenticationDetails {

    private RedisUtil redisUtil;

    // 验证码是否正确
    boolean flag = false;

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public CaptchaWebAuthenticationDetails(HttpServletRequest request,RedisUtil redisUtil) {
        super(request);
        this.setRedisUtil(redisUtil);
        validate(request);
        // 这里设置了 redisUtil 会导致序列化 springsecuritycontext 时，包含 redisUtil 这个类，这个类不能被序列化，所以用完就干掉
        this.setRedisUtil(null);

    }

    private void validate(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");
        String validCode = request.getParameter("validCode");

        // 校验一下随机验证码
        String validCodeByRedis = (String) redisUtil.get(String.format(RedisKeyContants.VALIDCODEKEY, uuid));
        if (validCode.equals(validCodeByRedis)) {
            flag = true;
            redisUtil.del(String.format(RedisKeyContants.VALIDCODEKEY, uuid));
        }
    }
}
