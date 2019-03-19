package com.yunji.controller;

import com.qcloud.cos.utils.Base64;
import com.yunji.model.ReturnBean;
import com.yunji.model.Tag;
import com.yunji.model.User;
import com.yunji.model.UserWxInfo;
import com.yunji.service.IUserService;
import net.sf.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.Utils;
import util.ValidationPatternEnum;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;


@Controller
@RequestMapping("/auth")
public class UserController {
    private  static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private IUserService userService;

    /**
     * 云笈扫码登录
     * @param code
     * @return
     */
    @RequestMapping("/wxLogin")
    @ResponseBody
    public ReturnBean weLogin(@RequestParam(value="code") String code) {
        logger.info("code{}:",code);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> usermap = new HashMap<>();
        try{
            Map<String, Object> authMap = userService.getAccessToken(code);
            //获取token失败
            if (authMap.containsKey("errcode")) {
                returnBean.setRetVal((Integer) authMap.get("errcode"));
                returnBean.setRetMsg((String)authMap.get("errmsg"));
                //成功
            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("accessToken", (String) authMap.get("accessToken"));
                param.put("openId", (String) authMap.get("openId"));
                param.put("refreshToken",(String) authMap.get("refreshToken"));
                //获取用户详细信息
                Map<String, Object> userInfo = userService.getWxUserInfo(param);
                if(param.containsKey("errcode")){
                    returnBean.setRetVal( (Integer) userInfo.get("errcode"));
                    returnBean.setRetMsg((String)userInfo.get("errmsg"));
                }else {
                    String unionId = (String) userInfo.get("unionid");
                    //检查用户是否已经注册
                    String userId = userService.checkUserExist(unionId);
                    //没有注册过，保存用户信息
                    if (StringUtils.isEmpty(userId)) {
                        User user = new User();
                        UserWxInfo wxInfo = new UserWxInfo();
                        user.setRegistryWay("wx");
                        user.setRegistryId("1");
                        wxInfo.setCountry((String) userInfo.get("country"));
                        wxInfo.setProvince((String)userInfo.get("province"));
                        wxInfo.setCity((String)userInfo.get("city"));
                        wxInfo.setSex((Integer) userInfo.get("sex"));
                        wxInfo.setNickname((String)userInfo.get("nickname"));
                        wxInfo.setOpenId((String)userInfo.get("openId"));
                        wxInfo.setUnionId((String)userInfo.get("unionid"));
                        wxInfo.setHeadimageurl((String)userInfo.get("headimgurl"));
                        Map<String,Object> resultMap = userService.saveUserInfo(user, wxInfo);
                        if (!StringUtils.isEmpty(resultMap.get("userId"))) {
                            usermap.put("userId", resultMap.get("userId"));
                            usermap.put("nickname", userInfo.get("nickname"));
                            usermap.put("headimageurl", userInfo.get("headimgurl"));
                        } else {
                            returnBean.setRetVal((Integer) resultMap.get("errocode"));
                            returnBean.setRetMsg((String)resultMap.get("errmsg"));
                        }
                        //注册过
                    } else {
                        usermap.put("userId", userId);
                        usermap.put("nickname", userInfo.get("nickname"));
                        usermap.put("headimageurl", userInfo.get("headimgurl"));
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("code{},errormsg{}",code,e.getMessage());
        }
        returnBean.setRetBean(usermap);
        logger.info("code{},usermap{}",code,usermap);
        return returnBean;
    }

    /**
     * 微信小程序登录验证接口
     * @param param
     * @return
     */
    @RequestMapping(value="/savewxinfo")
    @ResponseBody
    public ReturnBean wxAuthorize(String param){
        System.out.println(param);
        logger.info(param);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isEmpty(param)){
            returnBean.setRetMsg("param is empty");
            return returnBean;
        }
        String encryptedData = (String) Utils.getJsonValue(param,"encryptedData");
        String iv = (String)Utils.getJsonValue(param,"iv");
        String sessionkey = null;
        Map<String,Object> retMap = userService.getUnionToken((String.valueOf(Utils.getJsonValue(param.trim(),"code"))));
        System.out.println(retMap);
        if(retMap.containsKey("session_key")){
            sessionkey = (String) retMap.get("session_key");
        }
        String userInfoStr = this.getUserInfo(encryptedData,iv,sessionkey);
        System.out.println("userInfoStr:" + userInfoStr);
        String openId = (String)Utils.getJsonValue(userInfoStr,"openId");
        String unionId = (String)Utils.getJsonValue(userInfoStr,"unionId");
        String nickname = (String)Utils.getJsonValue(userInfoStr,"nickName");
        String province = (String)Utils.getJsonValue(userInfoStr,"province");
        String country = (String)Utils.getJsonValue(userInfoStr,"country");
        String city = (String)Utils.getJsonValue(userInfoStr,"city");
        String avatarUrl = (String)Utils.getJsonValue(userInfoStr,"avatarUrl");
        Integer sex = (Integer) Utils.getJsonValue(userInfoStr,"gender");
        User user = new User();
        user.setRegistryId("1");
        user.setRegistryWay("wx");
        UserWxInfo wxInfo = new UserWxInfo();
        wxInfo.setUnionId(unionId);
        wxInfo.setOpenId(openId);
        wxInfo.setHeadimageurl(avatarUrl);
        wxInfo.setNickname(nickname);
        wxInfo.setCountry(country);
        wxInfo.setProvince(province);
        wxInfo.setCity(city);
        wxInfo.setSex(sex);
        String userId = userService.checkUserExist(unionId);
        System.out.println("userId:" + userId);
        if(StringUtils.isEmpty(userId)){
            result = userService.saveUserInfo(user,wxInfo);
            result.put("openid",openId);
            returnBean.setRetBean(result);
        }else{
            result.put("openid",openId);
            result.put("userId",userId);
            returnBean.setRetBean(result);
        }
        return returnBean;
    }


// 需要修改userId 为username 用户名
    @RequestMapping(value="login")
    @ResponseBody
    public ReturnBean login(@RequestBody String param){
        logger.info("param");
        ReturnBean returnBean = new ReturnBean();
        String username = null;
        String password = null;
        if(!StringUtils.isEmpty(param)){
            JSONObject jsonObject = JSONObject.fromObject(param);
            if(jsonObject.containsKey("username")){
                username = (String) jsonObject.get("username");
//                if(!userId.matches(ValidationPatternEnum.USERID.getPattern())){
//                    logger.error("userId is wrong pattern");
//                    returnBean.setRetVal(108);
//                    returnBean.setRetMsg("userId is wrong pattern");
//                    return  returnBean;
//                }
            }else {
                returnBean.setRetVal(107);
                returnBean.setRetMsg("username is empty");
                logger.error("username is empty");
                return returnBean;
            }
            if(jsonObject.containsKey("password")){
                password = (String)jsonObject.get("password");
            }else {
                returnBean.setRetVal(108);
                returnBean.setRetMsg("password is empty");
                logger.error("password is empty");
                return  returnBean;
            }
        }else{
            returnBean.setRetVal(101);
            returnBean.setRetMsg("param is empty");
            logger.error("no param");
            return  returnBean;
        }
        User user = userService.checkUserPassword(username,password);
        if(null!=user&&!StringUtils.isEmpty(String.valueOf(user.getUserId()))){
            returnBean.setRetBean(user);
        }else{
            returnBean.setRetVal(110);
            returnBean.setRetMsg("密码不正确，请重新输入");
        }

        return returnBean;
    }

    public String getUserInfo(String encryptedData,String iv,String sessionkey){
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionkey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return result;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;


    }



}
