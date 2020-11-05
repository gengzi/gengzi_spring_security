package fun.gengzi.gengzi_spring_security.sys.entity;

import javax.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sys_permission")
public class SysPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //主键
    @Column(name = "id")
    private Long id;
    //父id
    @Column(name = "pid")
    private Long pid;
    //菜单名称
    @Column(name = "name")
    private String name;
    //类型   0：菜单   1：按钮
    @Column(name = "type")
    private Integer type;
    //
    @Column(name = "sort")
    private Integer sort;
    //菜单URL
    @Column(name = "url")
    private String url;
    //权限标识，如：sys:menu:save
    @Column(name = "permission")
    private String permission;
    //菜单图标
    @Column(name = "icon")
    private String icon;
    //状态
    @Column(name = "status")
    private Integer status;
    //说明
    @Column(name = "remark")
    private String remark;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    //创建者
    @Column(name = "creator")
    private String creator;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
    //更新人
    @Column(name = "updater")
    private String updater;
}