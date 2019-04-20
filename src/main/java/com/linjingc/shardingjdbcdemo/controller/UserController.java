package com.linjingc.shardingjdbcdemo.controller;

import com.linjingc.shardingjdbcdemo.dao.UserDao;
import com.linjingc.shardingjdbcdemo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author cxc
 * @date 2019/4/20 16:04
 */

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserDao userDao;


    @RequestMapping("save")
    public String save() {
        for (int i = 1; i < 100; i++) {
            //批量保存
            userDao.save(new User(i, "小明" + Integer.toString(i), Integer.toBinaryString(i)));
            System.out.println("保存第"+i+"条成功");
        }
        return "操作成功";
    }

    @RequestMapping("noDividedSave")
    public String noDividedSave() {
            userDao.save(new User(1, "不分片数据保存", Integer.toBinaryString(1)));
        return "操作成功";
    }


    @RequestMapping("find")
    public String save(Integer id) {
        User user=new User();
        boolean flag = userDao.existsById(id);
            if(flag){
                Optional<User> byId = userDao.findById(id);
                 user = byId.get();

            }
        return user.toString();
    }
}
