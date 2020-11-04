package fun.gengzi.gengzi_spring_security.sys.service;

import fun.gengzi.gengzi_spring_security.vo.ReturnData;

public interface UsersService {

    ReturnData loadUserByUsername(String userName);

}
