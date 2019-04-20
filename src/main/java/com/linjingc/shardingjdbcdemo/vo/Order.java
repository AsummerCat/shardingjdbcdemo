package com.linjingc.shardingjdbcdemo.vo;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cxc
 * @date 2019/4/20 15:58
 */
@Entity
@Table(name = "t_order")
@Data
public class Order {

    @Id
    private Integer id;
    private String name;
    private String age;

    public Order() {

    }

    public Order(Integer id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
