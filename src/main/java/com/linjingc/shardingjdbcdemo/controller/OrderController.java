package com.linjingc.shardingjdbcdemo.controller;

import com.linjingc.shardingjdbcdemo.dao.OrderDao;
import com.linjingc.shardingjdbcdemo.vo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author cxc
 * @date 2019/4/20 16:04
 */

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderDao orderDao;

    /**
     * 不分片保存
     *
     * @return
     */
    @RequestMapping("noDividedSave")
    public String noDividedSave() {
        for (int i = 1; i < 100; i++) {
            //批量保存
            orderDao.save(new Order(i, "不分片数据保存" + Integer.toString(i), Integer.toBinaryString(i)));
            System.out.println("保存第" + i + "条成功");
        }
        return "操作成功";
    }


    @RequestMapping("find")
    public String save(Integer id) {
        Order order = new Order();
        boolean flag = orderDao.existsById(id);
        if (flag) {
            Optional<Order> byId = orderDao.findById(id);
            order = byId.get();

        }
        return order.toString();
    }
}
