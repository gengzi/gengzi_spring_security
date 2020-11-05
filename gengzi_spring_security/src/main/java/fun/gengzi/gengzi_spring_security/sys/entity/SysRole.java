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
@Table(name = "sys_role")
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //主键
    @Column(name = "id")
    private Long id;
    //角色名称
    @Column(name = "name")
    private String name;
    //角色code
    @Column(name = "role_code")
    private String roleCode;
    //说明
    @Column(name = "remark")
    private String remark;
    //排序
    @Column(name = "sort")
    private Integer sort;
    //删除标识  0：未删除    1：删除
    @Column(name = "del_flag")
    private Integer delFlag;
    //创建者
    @Column(name = "creator")
    private String creator;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    //
    @Column(name = "updater")
    private String updater;
    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}