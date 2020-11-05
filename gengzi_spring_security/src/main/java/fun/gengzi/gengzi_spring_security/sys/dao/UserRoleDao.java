package fun.gengzi.gengzi_spring_security.sys.dao;

import fun.gengzi.gengzi_spring_security.sys.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleDao extends JpaRepository<UserRole, Long> {

    List<UserRoleDao> findByUserId(Long userId);

}