package com.yunji.dao;

import com.yunji.model.Interest;
import com.yunji.model.Organization;
import com.yunji.model.User;
import com.yunji.model.UserWxInfo;

import java.util.List;
import java.util.Map;

public interface AccountMapper {

    User selectUser(String username);

    void saveUser(User user);

    Integer getInterestAmout(String userId);

    List<Interest> getInterestList(Map<String,Object> param);

    Integer deleteInterest(Interest interest);

    Integer deleteAllInterest(String userId);

    Integer deleteAllAction(String userId);

    Integer deleteAllComments(String userId);

    UserWxInfo getWxInfo(String userId);

    Integer deleteInterestById(Integer id);

    Integer getFocusMeAmount(String userId);

    List<Interest> getFocusMeList(Map<String,Object> param);

    Integer saveOrganization(Organization organization);

    Integer deleteOrganization(Organization organization);

    List<Organization> getOrganizationList(Map<String,Object> param);
}
