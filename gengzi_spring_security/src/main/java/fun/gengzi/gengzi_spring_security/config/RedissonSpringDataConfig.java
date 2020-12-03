package fun.gengzi.gengzi_spring_security.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.io.IOException;

/**
 * <h1>redisson 配置</h1>
 *
 * 主要配置：
 * 0， 管理 httpsession 的配置，过期时间等
 * 1， 配置 redisson 的连接
 * 2， 配置 sessionid 的解析器，通过请求头中的sessionid来匹配 session 信息
 * 3， 配置 Redis key和value的序列化方式，注意，如果使用Jackson2JsonRedisSerializer 需要将 spring session 升级至 5.3 版本，否则会出现解析报错
 *
 *
 *
 *
 *
 * @author gengzi
 * @date 2020年11月3日14:59:45
 * <p>
 * 参考
 * https://github.com/redisson/redisson/wiki/14.-Integration-with-frameworks#147-spring-session
 */
@Configuration
// 启用redis管理会话
// maxInactiveIntervalInSeconds 最大的session 失效时间（秒）
// redisNamespace 命名空间 默认 spring:session
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800, redisNamespace = "gengzi:session")
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

//    /**
//     * 自定义RedisSerializer
//     *
//     * @return
//     */
//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        // 返回您的Redis序列化器实现
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        // 解决查询缓存转换异常的问题
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        return jackson2JsonRedisSerializer;
//    }

    /**
     * httpsessionid 解析器
     * 可以对比 cookie session 的形式，现在把 cookie 中存储 sessionid 。变成了请求头中存储的 token
     *
//     * @return
//     */
//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        return HeaderHttpSessionIdResolver.authenticationInfo();
//    }

//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }


}