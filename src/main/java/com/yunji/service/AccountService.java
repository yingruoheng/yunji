package com.yunji.service;

import com.yunji.model.Interest;
import com.yunji.model.Organization;
import com.yunji.model.User;
import com.yunji.model.UserWxInfo;

import java.util.List;


public interface AccountService {

    User getUser(String username);

    Integer saveAccount(User user);

    Integer getInterestAmount(String userId);

    List<Interest> getInterestList(String userId,String size,String createTime);

    Integer deleteInterest(Interest interest);

    Integer deleteUser(String userId);

    UserWxInfo getWxinfo(String userId);

    Integer deleteInterest(Integer id);

    Integer getFocusMeAmount(String userId);

    List<Interest> getFocusMeList(String userId,String size,String createTime);

    Integer saveOrganization(Organization organization);

    Integer deleteOrganization(Organization organization);

    List<Organization> getOrganizationList(String userId,String size,String createtime,String circle);


}
