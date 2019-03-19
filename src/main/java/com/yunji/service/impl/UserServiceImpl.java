package com.yunji.service.impl;

import com.yunji.dao.IUserDao;
import com.yunji.dao.UserWxInfoDao;
import com.yunji.model.Tag;
import com.yunji.model.User;
import com.yunji.model.UserWxInfo;
import com.yunji.service.IUserService;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.SysConfig;
import util.Utils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service("userService")
public class UserServiceImpl implements IUserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private IUserDao userDao;

    @Resource
    private UserWxInfoDao userWxInfoDao;


    @Override
    public HashMap<String,Object> getAccessToken(String code) {
        logger.info("code:{}",code);
        HashMap<String,Object> retMap = new HashMap<String,Object>();
        if(StringUtils.isEmpty(code)){
            logger.error("user does not authorized");
            return retMap;
        }
        CloseableHttpClient httpClient = null;
        String wexinUrl = SysConfig.getString("wxTokenUrl");
        try {
            httpClient = HttpClients.createDefault();
            String appid = SysConfig.getString("appid");
            String secret = SysConfig.getString("secret");
            String wechatUrl = Utils.joinString(wexinUrl,"appid=",appid,"&secret=",secret,"&code=",code,"&grant_type=authorization_code");
            HttpGet httpGet = new HttpGet(wechatUrl);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                StringBuilder builder = new StringBuilder();
                for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                    builder.append(temp);
                }
                Integer errcode = (Integer) Utils.getJsonValue(builder.toString(),"errcode");
                if(null!=errcode){
                    retMap.put("errcode",errcode);
                    retMap.put("errmsg",(String)Utils.getJsonValue(builder.toString(),"errmsg"));
                }else {
                    String accessToken = (String)Utils.getJsonValue(builder.toString(),"access_token");
                    String openId = (String)Utils.getJsonValue(builder.toString(),"openid");
                    String refreshToken = (String)Utils.getJsonValue(builder.toString(),"refresh_token");
                    Integer expires_in = (Integer) Utils.getJsonValue(builder.toString(),"expires_in");
                    String scope = (String) Utils.getJsonValue(builder.toString(),"scope");
                    //token过期时间
                    long expiresSeconds = System.currentTimeMillis()+expires_in*1000;
                    Date expireTime = new Date(expiresSeconds);
                    retMap.put("accessToken",accessToken);
                    retMap.put("openId",openId);
                    retMap.put("refreshToken",refreshToken);
                    retMap.put("expireTime",expireTime);
                    retMap.put("scope",scope);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("code{},errormsg{}",code,e.getMessage());
        }finally {
            try{
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
                logger.error("code{},errormsg",code,e.getMessage());
            }
        }
        return retMap;
    }

    @Override
    public Map<String,Object> getWxUserInfo(Map<String, Object> param) {
        logger.info("param");
        Map<String,Object> retMap = new HashMap<>();
        String baseurl = SysConfig.getString("wxUserUrl");
        String openId = null;
        String accessToken = null;
        String appid = SysConfig.getString("appid");
        String secret = SysConfig.getString("secret");
        if(param.containsKey("openId")){
            openId = (String) param.get("openId");
        }
        if(param.containsKey("accessToken")){
            accessToken = (String) param.get("accessToken");
        }
        String wxUrl = Utils.joinString(baseurl,"access_token=",accessToken,"&openid=",openId);
        CloseableHttpClient closeableHttpClient = null;
        try{
            closeableHttpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(wxUrl);
            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == SysConfig.getInteger("code.success")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                    builder.append(temp);
                }
                Integer errcode = (Integer) Utils.getJsonValue(builder.toString(),"errcode");
                if(null!=errcode){
                    retMap.put("errcode",errcode);
                    retMap.put("errmsg",(String)Utils.getJsonValue(builder.toString(),"errmsg"));
                }else {
                    String nickname = (String) Utils.getJsonValue(builder.toString(),"nickname");
                    Integer sex = (Integer) Utils.getJsonValue(builder.toString(),"sex");
                    String province = (String) Utils.getJsonValue(builder.toString(),"province");
                    String city = (String) Utils.getJsonValue(builder.toString(),"city");
                    String country = (String) Utils.getJsonValue(builder.toString(),"country");
                    String headimgurl = (String) Utils.getJsonValue(builder.toString(),"headimgurl");
                    String unionid = (String) Utils.getJsonValue(builder.toString(),"unionid");
                    String subscribe_time = (String) Utils.getJsonValue(builder.toString(),"subscribe_time");
                    retMap.put("nickname",nickname);
                    retMap.put("province",province);
                    retMap.put("sex",sex);
                    retMap.put("country",country);
                    retMap.put("city",city);
                    retMap.put("headimgurl",headimgurl);
                    retMap.put("unionid",unionid);
                    retMap.put("subscribe_time",subscribe_time);
                    retMap.put("openId",openId);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            logger.error("openId{},errormsg{}",openId,e.getMessage());
        }finally {
            try{
                closeableHttpClient.close();
            }catch (IOException e){
                e.printStackTrace();
                logger.error("openId{},errormsg{}",openId,e.getMessage());
            }

        }
        logger.info("openId{},usermap{}",openId,retMap);
        return retMap;
    }


    @Override
    public String checkUserExist(String unionId) {
        logger.info("openid{}:",unionId);
        UserWxInfo userWxInfo = userWxInfoDao.SelectByUnionId(unionId);
        String userId = null;
        if(null!=userWxInfo){
            userId = userWxInfo.getUserId().toString();
        }
        logger.info("openid{},userId{}",unionId,userId);
        return userId;
    }

    @Transactional
    @Override
    public Map<String,Object> saveUserInfo(User user,UserWxInfo userWxInfo) {
        logger.info("user{},userWxInfo{}",user,userWxInfo);
        Map<String,Object> param = new HashMap<String, Object>();
        Integer userId = null;
            if(null!=user||null!=userWxInfo){
                int i = userDao.insertOne(user);
                if(i>0){
                    userId = user.getUserId();
                    if(!StringUtils.isEmpty(userId.toString())){
                        userWxInfo.setUserId(userId);
                        try{
                            String nickname = Base64.encodeBase64String(userWxInfo.getNickname().getBytes("utf-8"));
                            userWxInfo.setNickname(nickname);
                        }catch (Exception e){

                        }
                        int wxResult = userWxInfoDao.insertOne(userWxInfo);
                        if(wxResult == 1){
                            param.put("userId",userId.toString());
                        }else{
                            logger.error("insert into wxinfo failed");
                        }
                    }else {
                        logger.error("insert into user failed");
                    }
                }
            }
        logger.info("userId{}",userId);
        return param;
    }

//    @Override
//    public List<Tag> getUserTag(String userId) {
//        logger.info("userId{}",userId);
//        List<Tag> userTag = null;
//        userTag = userDao.selectUserTag(userId);
//        logger.info("userId{},userTag{}",userId,userTag);
//        return userTag;
//    }

    @Override
    public User getUserInfo(String userId) {
        User user = userDao.selectUser(userId);
        return user;
    }

    @Override
    public User checkUserPassword(String username, String password) {
        User user = null;
        if(!StringUtils.isEmpty(username)||!StringUtils.isEmpty(password)){
            user = userDao.getUserByName(username);

            if(user != null &&!StringUtils.isEmpty(user.getPassword())){
                if(user.getPassword().equals(password)){
                    return user;
                }else {
                    return new User();
                }
            }else{
                logger.error("password get from  DB is null");
            }
        }else {
            logger.error("userId or password is empty");
        }

        return user;
    }

    @Override
    public UserWxInfo getWxinfo(String userId) {
        UserWxInfo wxInfo = new UserWxInfo();
        wxInfo = userWxInfoDao.selectByUserId(userId);
        if(null!=wxInfo){
            try {
                String nickname = wxInfo.getNickname();
                wxInfo.setNickname(new String(Base64.decodeBase64(nickname.getBytes()),"utf-8"));
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return wxInfo;
    }

    @Override
    public Map<String, Object> getUnionToken(String code) {
        System.out.println(code);
        Map<String,Object> param = new HashMap<>();
        String wexinUrl = SysConfig.getString("wxtokenUrl");
        String appid = SysConfig.getString("wx_appid");
        String secret = SysConfig.getString("wx_secret");
//        String appid = "wxf6cf38cc52fb2bf0";
//        String secret = "b873b453acc8e4eab1089b4d9ee6abac";
        String wechatUrl = Utils.joinString(wexinUrl,"appid=",appid,"&secret=",secret,"&js_code=",code,"&grant_type=authorization_code");
        System.out.println(wechatUrl);
        CloseableHttpClient closeableHttpClient = null;
        try{
            closeableHttpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(wechatUrl);
            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                    builder.append(temp);
                }
                Integer errcode = (Integer) Utils.getJsonValue(builder.toString(), "errcode");
                if (null != errcode) {
                    param.put("errcode", errcode);
                    param.put("errmsg", (String) Utils.getJsonValue(builder.toString(), "errMsg"));
                } else {
                    String unionid = (String) Utils.getJsonValue(builder.toString(), "unionid");
                    String openid = (String) Utils.getJsonValue(builder.toString(), "openid");
                    String session_key = (String) Utils.getJsonValue(builder.toString(), "session_key");
                    param.put("unionid", unionid);
                    param.put("session_key", session_key);
                    param.put("openid", openid);
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                closeableHttpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return param;
    }

    public static void main(String args[]){
        String name = "像风走了八千里";
        try {
            String encod = Base64.encodeBase64String(name.getBytes("utf-8"));
            System.out.println("encod"+encod);
            String decod = new String(Base64.decodeBase64(encod.getBytes()),"utf-8");
            System.out.println("decod: "+decod);

        }catch (Exception e){
            e.printStackTrace();
        }


    }


}



