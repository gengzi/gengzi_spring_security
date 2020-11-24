package fun.gengzi.gengzi_spring_security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <h1>密码的加密方式</h1>
 *
 * @author gengzi
 * @date 2020年11月24日10:53:14
 */
@Configuration
@AllArgsConstructor
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 推荐使用 BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

}
