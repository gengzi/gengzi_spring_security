package fun.gengzi.gengzi_spring_security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

///**
// * TokenStore
// */
//@Configuration
//@AllArgsConstructor
//public class TokenStoreConfig {
//    private RedisConnectionFactory redisConnectionFactory;
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }
//}
