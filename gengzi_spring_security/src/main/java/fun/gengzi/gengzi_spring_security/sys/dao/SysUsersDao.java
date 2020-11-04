package fun.gengzi.gengzi_spring_security.sys.dao;

import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SysUsersDao extends JpaRepository<SysUsers, Integer> {

    SysUsers findByUsername(String username);

}
