package com.linjingc.shardingjdbcdemo.dao;

import com.linjingc.shardingjdbcdemo.vo.Order;
import org.springframework.data.repository.CrudRepository;

/**
 * @author cxc
 * @date 2019/4/20 16:02
 */
public interface OrderDao extends CrudRepository<Order, Integer> {
}
