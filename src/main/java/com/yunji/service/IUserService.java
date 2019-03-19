package com.yunji.service;

import com.yunji.model.ReturnBean;
import com.yunji.model.Tag;
import com.yunji.model.User;
import com.yunji.model.UserWxInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IUserService {

    String checkUserExist(String unionId);

    Map<String,Object> getAccessToken(String code);

    Map<String,Object> getWxUserInfo(Map<String,Object> param);

    Map<String,Object> saveUserInfo(User user, UserWxInfo wxInfo);

//    List<Tag> getUserTag(String userId);

    User getUserInfo(String userId);

    User checkUserPassword(String username,String password);

    UserWxInfo getWxinfo(String userId);

    Map<String,Object> getUnionToken(String code);





}

