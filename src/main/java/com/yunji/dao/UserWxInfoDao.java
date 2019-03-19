package com.yunji.dao;

import com.yunji.model.UserWxInfo;

public interface UserWxInfoDao {

    UserWxInfo SelectByUnionId(String unionId);

    int insertOne(UserWxInfo userWxInfo);

    Integer deleteWxinfo(String userId);

    UserWxInfo selectByUserId(String userId);



}
