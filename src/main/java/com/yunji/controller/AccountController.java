package com.yunji.controller;

import com.yunji.model.*;
import com.yunji.service.AccountService;
import com.yunji.service.ArticleService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import util.Utils;
import util.ValidationPatternEnum;

import java.util.*;

@Controller
@RequestMapping(value="/account")
public class AccountController {
    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;


    @RequestMapping(value="/registry")
    @ResponseBody
    //用户名密码登录接口
    public ReturnBean singup(@RequestBody String param){
        logger.info(param);
        ReturnBean returnBean = new ReturnBean();
        JSONObject jsonObject = JSONObject.fromObject(param);
        String username = null;
        String password = null;
        if(jsonObject.containsKey("username")){
            username = (String)jsonObject.get("username");
            User user = accountService.getUser(username);
            if(null != user&&user.getUsername().equals(username)){
                logger.error("This username have already registered");
                returnBean.setRetVal(111);
                returnBean.setRetMsg("This username have already registered");
                return returnBean;
            }
        }else{
            logger.error("username is empty");
            returnBean.setRetVal(107);
            returnBean.setRetMsg("username is empty");
            return returnBean;
        }
        if(jsonObject.containsKey("password")){
            // 给password设置格式，并校验
            password = (String) jsonObject.get("password");
        }else{
            logger.error("password");
            returnBean.setRetVal(108);
            returnBean.setRetMsg("password is empty");
            return returnBean;
        }
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        user.setRegistryWay("用户名密码");
        user.setRegistryWay("2");
        Integer userId = accountService.saveAccount(user);
        if(StringUtils.isEmpty(String.valueOf(userId))){
            logger.error("register failed");
            returnBean.setRetVal(112);
            returnBean.setRetMsg("register failed");
        }else{
            logger.info("userId: "+userId+" register successfully");
            returnBean.setRetBean(userId);
            returnBean.setRetBean(userId);
        }

        return  returnBean;

    }

//我的关注数接口
    @RequestMapping(value = "/amount")
    @ResponseBody
    public ReturnBean getInterestAmount(@RequestParam("userId") String userId){
        long time1 = System.currentTimeMillis();
        logger.info(userId);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
            Integer amount = 0;
            amount = accountService.getInterestAmount(userId);
            param.put("amount",amount);
            returnBean.setRetBean(param);
        }else {
            returnBean.setRetVal(1);
            returnBean.setRetMsg("userId 格式错误");
        }
        long time2 = System.currentTimeMillis();
        long time = time2-time1;
        System.out.println("获取用户关注数时间: "+time);
        return returnBean;
    }

    //关注我的数量
    @RequestMapping(value = "/focusme")
    @ResponseBody
    public ReturnBean getFocusmeAmount(@RequestParam("userId") String userId){
        long time1 = System.currentTimeMillis();
        logger.info(userId);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
            Integer amount = 0;
            amount = accountService.getFocusMeAmount(userId);
            param.put("amount",amount);
            returnBean.setRetBean(param);
        }else {
            returnBean.setRetVal(1);
            returnBean.setRetMsg("userId 格式错误");
        }
        long time2 = System.currentTimeMillis();
        long time = time2-time1;
        System.out.println("关注用户的数量: "+time);
        return returnBean;
    }

    @RequestMapping("/mineData")
    @ResponseBody
    public ReturnBean getMineData(String userId){
        ReturnBean returnBean = new ReturnBean();
        long time1 = System.currentTimeMillis();
        Integer myFocus = null;
        Integer focusMe = null;
        Integer articles = null;
        logger.info(userId);
        Map<String,Object> param = new HashMap<>();
        if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
            Integer amount = 0;
            myFocus = accountService.getInterestAmount(userId);
            focusMe = accountService.getFocusMeAmount(userId);
            articles = articleService.getUserArticleAmount(userId);
            param.put("myFocus",myFocus);
            param.put("focusMe",focusMe);
            param.put("articles",articles);
            returnBean.setRetBean(param);
        }else {
            returnBean.setRetVal(1);
            returnBean.setRetMsg("userId 格式错误");
        }
        long time2 = System.currentTimeMillis();
        long time = time2-time1;
        System.out.println("获取用户关注数时间: "+time);
        return returnBean;


    }


    @RequestMapping(value="/interest")
    @ResponseBody
    public ReturnBean getInterestList(@RequestParam("userId") String userId,@RequestParam(value = "size", required = false) String size,
                                      @RequestParam(value="createTime",required = false) String createTime){
        logger.info("userId{},size{},createTime{}",userId,size,createTime);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
            if(!StringUtils.isEmpty(size)){
                if(!size.matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())){
                    returnBean.setRetVal(100);
                    returnBean.setRetMsg("size is wrong pattern");
                }
            }
            List<Interest> interestList = accountService.getInterestList(userId,size,createTime);
            if(interestList.size()> 0){
                for(int i=0;i<interestList.size();i++){
                    if(!StringUtils.isEmpty(interestList.get(i).getToUserId())){
                        UserWxInfo wxInfo = accountService.getWxinfo(String.valueOf(interestList.get(i).getToUserId()));
                        if(null!=wxInfo){
                            interestList.get(i).setToUserImage(wxInfo.getHeadimageurl());
                            interestList.get(i).setToUsername(wxInfo.getNickname());
                        }

                    }
                    Utils.setProperty(interestList.get(i));
                }
            }
            param.put("interestList",interestList);
            returnBean.setRetBean(param);

        }else {
            returnBean.setRetVal(1);
            returnBean.setRetMsg("userId 格式错误");
        }

        return returnBean;

    }

    @RequestMapping(value="/focusMeList")
    @ResponseBody
    public ReturnBean getFocusMeList(@RequestParam("userId") String userId,@RequestParam("size") String size,
                                      @RequestParam(value="createTime",required = false) String createTime){
        logger.info("userId{},size{},createTime{}",userId,size,createTime);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
            if(size.matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())){
                List<Interest> focusMeList = accountService.getFocusMeList(userId,size,createTime);
                if(focusMeList.size()!=0){
                    for(int i=0;i<focusMeList.size();i++){
                        UserWxInfo wxInfo = accountService.getWxinfo(String.valueOf(focusMeList.get(i).getUserId()));
                        if(null!=wxInfo){
                            focusMeList.get(i).setUsername(wxInfo.getNickname());
                            focusMeList.get(i).setUserImage(wxInfo.getHeadimageurl());
                            focusMeList.get(i).setToUserImage(wxInfo.getHeadimageurl());
                            focusMeList.get(i).setToUsername(wxInfo.getNickname());
                        }
                        Utils.setProperty(focusMeList.get(i));
                    }
                }
                param.put("focusMeList",focusMeList);
                logger.info("focusmeList{}",focusMeList);
                returnBean.setRetBean(param);
            }else{
                returnBean.setRetVal(2);
                returnBean.setRetMsg("size 格式错误");
            }
        }else {
            returnBean.setRetVal(1);
            returnBean.setRetMsg("userId 格式错误");
        }

        return returnBean;

    }




    @RequestMapping(value = "/deleteInterest", method=RequestMethod.GET)
    @ResponseBody
    public ReturnBean  deleteInterest(@RequestParam("id") String id){
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        Integer result = accountService.deleteInterest(Integer.valueOf(id));
        if(result == 1){
            param.put("result","删除成功");
            returnBean.setRetBean(param);
        }else {
            param.put("result","删除失败");
            returnBean.setRetBean(param);
        }
        return returnBean;
    }


    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET)
    @ResponseBody
    public ReturnBean deleteAccount(@RequestParam("userId") String userId){
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        if(StringUtils.isEmpty(userId)){
            returnBean.setRetVal(100);
            returnBean.setRetMsg("userId is empty");
        }else{
            if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
                Integer result = accountService.deleteUser(userId);
                if(result ==1){
                    param.put("result","删除成功");
                }else{
                    returnBean.setRetVal(001);
                    returnBean.setRetMsg("删除失败");
                }
                returnBean.setRetBean(param);
            }else {
                returnBean.setRetVal(101);
                returnBean.setRetMsg("userId in wrong pattern");
            }
        }
        return returnBean;
    }

    @RequestMapping(value = "/userInfo")
    @ResponseBody
    public ReturnBean getUserInfo(@RequestParam("userId") String userId){
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        User user = accountService.getUser(userId);
        UserWxInfo wxInfo = accountService.getWxinfo(userId);
        param.put("user",user);
        param.put("wxinfo",wxInfo);
        returnBean.setRetBean(param);
        return returnBean;
    }

    @RequestMapping("/saveOrganization")
    @ResponseBody
    public ReturnBean saveOrganization (@RequestBody Organization organization){
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> resultMap = new HashMap<>();
        if(organization == null){
            returnBean.setRetMsg("参数organization is valid");
        }else{
            if(StringUtils.isEmpty(organization.getUserId())){
                returnBean.setRetMsg("userId is valid");
            }
            if(StringUtils.isEmpty(organization.getOrganization())){
                returnBean.setRetMsg("orgnization is valid");
            }
            if(StringUtils.isEmpty(organization.getCircle())){
                returnBean.setRetMsg("circle is valid");
            }
            Integer result = accountService.saveOrganization(organization);
            if(result == 1||result == 2){
                resultMap.put("result","保存成功");
            }else{
                resultMap.put("result","保存失败");
            }
            returnBean.setRetBean(resultMap);
        }
        return returnBean;
    }

    @RequestMapping("/deleteOrganization")
    @ResponseBody
    public ReturnBean deleteOrganization(@RequestBody Organization organization){
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> resultMap = new HashMap<>();
        if(organization == null){
            returnBean.setRetMsg("参数organization is valid");
        }else{
            if(StringUtils.isEmpty(organization.getUserId())){
                returnBean.setRetMsg("userId is valid");
            }
            if(StringUtils.isEmpty(organization.getOrganization())){
                returnBean.setRetMsg("orgnization is valid");
            }
            if(StringUtils.isEmpty(organization.getCircle())){
                returnBean.setRetMsg("circle is valid");
            }
            Integer result = accountService.deleteOrganization(organization);
            if(result == 1){
                resultMap.put("result","删除成功");
            }else{
                resultMap.put("result","删除失败");
            }
            returnBean.setRetBean(resultMap);
        }
        return returnBean;
    }

    @RequestMapping("/organizationList")
    @ResponseBody
    public ReturnBean getOrganizationList(String userId,@RequestParam(value="size",required =false) String size,@RequestParam(value="createtime",required = false) String createtime,@RequestParam(value = "circle",required = false) String circle){
        logger.info("userId{},size{},createtime{}",userId,size,createtime);
        ReturnBean returnBean = new ReturnBean();
        if(StringUtils.isEmpty(userId)){
            returnBean.setRetMsg("userId is valid");
            logger.error("userId is valid");
        }else{
            if(!userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
                returnBean.setRetMsg("userId is wrong pattern");
                logger.error("userId is wrong pattern");
            }else{
                if(!StringUtils.isEmpty(size)){
                    if(!size.trim().matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())){
                        returnBean.setRetMsg("size is wrong pattern");
                        logger.error("size is wrong pattern");
                    }
                }
                List<Organization> organizations  = accountService.getOrganizationList(userId,size,createtime,circle);
                returnBean.setRetBean(organizations);
                logger.info("organization{}",organizations);
            }

        }

        return returnBean;


    }



}
