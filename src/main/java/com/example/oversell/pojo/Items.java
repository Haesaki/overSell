package com.example.oversell.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.security.auth.PrivateCredentialPermission;
import java.util.Date;


//-- auto-generated definition
//        create table items_version
//        (
//        id           varchar(64) not null comment '商品主键id'
//        primary key,
//        item_name    varchar(32) not null comment '商品名称',
//        stock        int         not null comment '库存',
//        created_time datetime    not null comment '创建时间',
//        updated_time datetime    not null comment '更新时间'
//        );

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items {
    private String id;
    private String itemName;
    private Integer stock;
    private Integer version;
    private Date createdTime;
    private Date updatedTime;
}
