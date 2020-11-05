package fun.gengzi.gengzi_spring_security.sys.dao;

import fun.gengzi.gengzi_spring_security.sys.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SysPermissionDao extends JpaRepository<SysPermission, Long> {


    @Transactional
    @Modifying
    @Query("SELECT t3 FROM UserRole t1 LEFT JOIN RolePermission t2 ON t1.roleId = t2.roleId LEFT JOIN SysPermission t3 ON t3.id = t2.permissionId WHERE t1.userId = ?1")
    List<SysPermission> qryPermissionInfoByUserId(Long userId);

}