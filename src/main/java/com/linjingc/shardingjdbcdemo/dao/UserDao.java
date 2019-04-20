package com.linjingc.shardingjdbcdemo.dao;

import com.linjingc.shardingjdbcdemo.vo.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author cxc
 * @date 2019/4/20 16:02
 */
public interface UserDao extends CrudRepository<User, Integer> {
}
