package fun.gengzi.gengzi_spring_security.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import java.io.IOException;

/**
 * <h1>redisson 配置</h1>
 *
 * @author gengzi
 * @date 2020年11月3日14:59:45
 *
 * 参考
 * https://github.com/redisson/redisson/wiki/14.-Integration-with-frameworks#147-spring-session
 */
@Configuration
// 启用redis管理会话
@EnableRedisHttpSession
public class RedissonSpringDataConfig extends AbstractHttpSessionApplicationInitializer {

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(@Value("classpath:/redisson.yml") Resource configFile) throws IOException {
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

}