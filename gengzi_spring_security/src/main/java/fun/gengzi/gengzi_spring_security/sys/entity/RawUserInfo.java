package fun.gengzi.gengzi_spring_security.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawUserInfo {

    private String gists_url;
    private String repos_url;
    private String following_url;
    private String twitter_username;
    private String bio;
    private Date created_at;
    private String login;
    private String type;
    private String blog;
    private String subscriptions_url;
    private Date updated_at;
    private boolean site_admin;
    private String company;
    private long id;
    private int public_repos;
    private String gravatar_id;
    private String email;
    private String organizations_url;
    private String hireable;
    private String starred_url;
    private String followers_url;
    private int public_gists;
    private String url;
    private String received_events_url;
    private int followers;
    private String avatar_url;
    private String events_url;
    private String html_url;
    private int following;
    private String name;
    private String location;
    private String node_id;
    

}