package fun.gengzi.gengzi_spring_security.token;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;


/**
 * 令牌
 *
 */
@Service
public class BaseTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if(accessToken instanceof DefaultOAuth2AccessToken){
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;

            //增加额外信息
//            Map<String, Object> info = Maps.newHashMap();
//            info.put("code", new Result<>().getCode());
//            token.setAdditionalInformation(info);

            return token;
        }

        return accessToken;
    }
}