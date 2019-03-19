package com.yunji.dao;

import com.yunji.model.Tag;
import com.yunji.model.User;

import java.util.List;

public interface IUserDao {

    User selectUser(String id);

    int insertOne(User user);

//    List<Tag> selectUserTag(String user);

    User getUserByName(String username);

    Integer deleteUser(String userId);


}
