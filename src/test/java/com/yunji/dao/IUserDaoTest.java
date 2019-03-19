package com.yunji.dao;

import com.yunji.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:45
 * desc
 */

// 加载spring配置文件
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml"})
public class IUserDaoTest {

    @Autowired
    private IUserDao dao;

//    @Test
//    public void testSelectUser(){
//        long id = 1;
//        User user = dao.selectUser(id);
//        System.out.println(user.getUsername());
//    }

//    @Test
//    public void saveUserInfo(){
//        User user = new User();
//        user.setWayId("1");
//        user.setWay("微信");
//        int result = dao.insertOne(user);
//        Integer userId = user.getUserId();
//        System.out.println("result: "+result);
//        System.out.println("userId: "+userId);
//    }

//    @Test
//    public void getUserTag(){
//        Integer userId = 10000006;
//        List<Tag> userTag = dao.selectUserTag(userId.toString());
//        System.out.println(userTag);
//
//    }




}
