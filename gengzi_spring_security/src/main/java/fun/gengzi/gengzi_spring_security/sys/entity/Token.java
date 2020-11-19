package fun.gengzi.gengzi_spring_security.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String accessToken;
    private int expireIn;
    private String refreshToken;
    private String uid;
    private String openId;
    private String accessCode;
    private String unionId;
    private String scope;
    private String tokenType;
    private String idToken;
    private String macAlgorithm;
    private String macKey;
    private String code;
    private String oauthToken;
    private String oauthTokenSecret;
    private String userId;
    private String screenName;
    private String oauthCallbackConfirmed;
}