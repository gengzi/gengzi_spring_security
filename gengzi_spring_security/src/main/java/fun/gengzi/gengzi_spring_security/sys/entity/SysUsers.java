package fun.gengzi.gengzi_spring_security.sys.entity;

import javax.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sys_users")
public class SysUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//主键
    @Column(name = "id")
    private Integer id;
    //登陆用户名
    @Column(name = "username")
    private String username;
    //真正用户名
    @Column(name = "realname")
    private String realname;
    //密码
    @Column(name = "password")
    private String password;
    //性别
    @Column(name = "gender")
    private Integer gender;
    //手机号
    @Column(name = "mobile")
    private String mobile;
    //
    @Column(name = "email")
    private String email;
    //创建者
    @Column(name = "create_user")
    private String createUser;
    //
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;
    //是否可用
    @Column(name = "is_enable")
    private Integer isEnable;
}