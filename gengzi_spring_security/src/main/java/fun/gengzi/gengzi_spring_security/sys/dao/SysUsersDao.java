package fun.gengzi.gengzi_spring_security.sys.dao;

import fun.gengzi.gengzi_spring_security.sys.entity.SysUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SysUsersDao extends JpaRepository<SysUsers, Long> {

    SysUsers findByUsername(String username);




}
