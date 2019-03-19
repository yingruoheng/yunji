package com.yunji.service.impl;

import com.yunji.dao.AccountMapper;
import com.yunji.dao.ArticleMapper;
import com.yunji.dao.IUserDao;
import com.yunji.dao.UserWxInfoDao;
import com.yunji.model.*;
import com.yunji.service.AccountService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.Utils;

import java.util.*;


@Service("accountService")
public class AccountServiceImpl implements AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserWxInfoDao wxInfoDao;





    @Override
    public User getUser(String userId) {
        User user = null;
        if(!StringUtils.isEmpty(userId)){
            user = accountMapper.selectUser(userId);
        }else {
            logger.error("usename is empty");
        }
        return user;
    }

    @Override
    public Integer saveAccount(User user) {
        Integer userId = null;
        if(StringUtils.isEmpty(user.getUsername())||StringUtils.isEmpty(user.getPassword())){
            logger.error("username or password is empty");
        }else{
            accountMapper.saveUser(user);
            userId = user.getUserId();
            if(StringUtils.isEmpty(String.valueOf(userId))){
                logger.error("save user failed");
            }
        }
        return userId;
    }

    @Override
    public Integer getInterestAmount(String userId) {
        Integer result = 0;
        result = accountMapper.getInterestAmout(userId);
        return result;
    }

    @Override
    public List<Interest> getInterestList(String userId,String size,String createTime) {
        List<Interest> interestList = new ArrayList<>();
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        Integer limit = null;
        if(!StringUtils.isEmpty(size)){
            param.put("limit",Integer.valueOf(size));
            limit = Integer.valueOf(size);
        }
        param.put("limit",limit);
        param.put("createTime",createTime);
        interestList = accountMapper.getInterestList(param);
        return interestList;
    }

    @Override
    public Integer deleteInterest(Interest interest) {
        logger.info("interest"+interest);
        Integer result = null;
        if(null!=interest){
            result = accountMapper.deleteInterest(interest);
        }
        return result;
    }

    @Override
    @Transactional
    public Integer deleteUser(String userId) {
        logger.info("userId"+userId);
        Integer userResult = userDao.deleteUser(userId);
        if(userResult == 1){
            Integer wxResult = wxInfoDao.deleteWxinfo(userId);
            if (wxResult >= 0) {
                Integer artiResult = articleMapper.deleteAllArticles(userId);
                if(artiResult>=0){
                    Integer intereResult = accountMapper.deleteAllInterest(userId);
                    if(intereResult>=0){
                        Integer actionResult = accountMapper.deleteAllAction(userId);
                        if(actionResult>=0){
                            Integer comResult = accountMapper.deleteAllComments(userId);
                            if(comResult>=0){
                                return userResult;
                            }else{
                                logger.error("delete comments faild");
                            }
                        }else{
                            logger.error("delete action failed");
                        }
                    }else{
                        logger.error("delete interest failed");
                    }
                }else{
                    logger.error("delete article failed");
                }
            }else{
                logger.error("delete wxinfo failed");
            }
        }else{
            logger.error("delete user failed");
        }
        return 0;
    }

    @Override
    public UserWxInfo getWxinfo(String userId) {
        UserWxInfo userWxInfo = null;
        if(!StringUtils.isEmpty(userId)){
            userWxInfo = accountMapper.getWxInfo(userId);
        }
        if(null!=userWxInfo){
            try {
                String nickname = userWxInfo.getNickname();
                userWxInfo.setNickname(new String(Base64.decodeBase64(nickname.getBytes()),"utf-8"));
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return userWxInfo;
    }

    @Override
    public Integer deleteInterest(Integer id) {
        Integer result = accountMapper.deleteInterestById(id);
        return result;
    }

    @Override
    public Integer getFocusMeAmount(String userId) {

        Integer result = accountMapper.getFocusMeAmount(userId);
        return result;



    }

    @Override
    public List<Interest> getFocusMeList(String userId,String size,String createTime) {
        List<Interest> interestList = new ArrayList<>();
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("limit",Integer.valueOf(size));
        param.put("createTime",createTime);
        interestList = accountMapper.getFocusMeList(param);
        return interestList;


    }

    @Override
    public Integer saveOrganization(Organization organization) {
        Integer result = accountMapper.saveOrganization(organization);
        return result;
    }

    @Override
    public Integer deleteOrganization(Organization organization) {
        Integer result = accountMapper.deleteOrganization(organization);
        return result;
    }

    @Override
    public List<Organization> getOrganizationList(String userId, String size, String createtime, String circle) {
        Map<String,Object> param = new HashMap<>();
        List<Organization> organizations = new ArrayList<>();
        Integer limit = null;
        Date time = null;
        if(!StringUtils.isEmpty(size)){
            limit = Integer.valueOf(size);
        }
        param.put("userId",userId);
        param.put("limit",limit);
        param.put("createtime",createtime);
        param.put("circle",circle);
        organizations = accountMapper.getOrganizationList(param);
        for(Organization organization:organizations){
            switch(organization.getCircle()){
                case "work":
                    organization.setCircle("工作人事");
                    break;
                case "training":
                    organization.setCircle("课业培训");
                    break;
                case "play":
                    organization.setCircle("健身休闲");
                    break;
                case "share":
                    organization.setCircle("家庭分享");
                    break;
            }
        }
        return organizations;
    }


}
