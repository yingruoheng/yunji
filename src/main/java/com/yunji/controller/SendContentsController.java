package com.yunji.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunji.model.Article;
import com.yunji.model.ReturnBean;
import com.yunji.service.ArticleService;
import com.yunji.service.IdStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import util.JedisUtils;
import util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@RequestMapping("/sendContents")
public class SendContentsController {
    public static Logger logger = LoggerFactory.getLogger(SendContentsController.class);

    @Autowired
    private IdStatusService idStatusService;
    @Autowired
    private ArticleService articleService;

    /*
    向redis写入模版内容
     */
    @RequestMapping("/contents")
    @ResponseBody
    private ReturnBean saveWeekly(@RequestBody String param){
//        Jedis jedis = new Jedis("62.234.179.133", 4000);
        Jedis jedis = new Jedis("192.168.1.12", 4000);

        ReturnBean rb = new ReturnBean();

        Integer articleId = idStatusService.saveStatus(666);

        System.out.println("articleId:" + articleId);

        System.out.println(param);
        JSONObject jsonObject = JSONObject.parseObject(param);

        Article article = new Article();

        String userId = jsonObject.getString("userId");

        System.out.println("userId:" + userId);
        String username = jsonObject.getString("username");
        System.out.println("username:" + username);
        String title = jsonObject.getString("title");
        System.out.println("title:" + title);
        String circle = jsonObject.getString("circle");
        String scenario = jsonObject.getString("scenario");
        System.out.println("scenario:" + scenario);
        String visibility = jsonObject.getString("visibility");
        String shareVisibility = jsonObject.getString("shareVisibility");
        String organization = jsonObject.getString("organization");
        String createtime = jsonObject.getString("createtime");
        String starttime = jsonObject.getString("starttime");
        //字符串转时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createDate = null;
        Date startDate = null;
        try {
            createDate = format.parse(createtime);
            createDate = Utils.FormatUTCTime(createDate);
            startDate = format.parse(starttime);
            startDate = Utils.FormatUTCTime(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String summary = "";
        if("train".equals(scenario)){
            JSONArray contents = jsonObject.getJSONArray("contents");
            for (int i=0; i<contents.size(); i++){
                JSONObject object = contents.getJSONObject(i);

                summary = object.getString("content");
            }

        }
        if("task".equals(scenario)){
            JSONArray contents = jsonObject.getJSONArray("contents");
            for (int i=0; i<contents.size(); i++){
                JSONObject object = contents.getJSONObject(i);

                summary = object.getString("content");
            }
        }
        if("activity".equals(scenario)){
            JSONArray contents = jsonObject.getJSONArray("contents");
            for (int i=0; i<contents.size(); i++){
                JSONObject object = contents.getJSONObject(i);

                summary = object.getString("content");
            }
        }
        if("weekly".equals(scenario)){
            JSONArray contents = jsonObject.getJSONArray("contents");
            for (int i=0; i<contents.size(); i++){
                JSONObject object = contents.getJSONObject(i);
                String thisWeekComplete = object.getString("thisWeekComplete");
                String thisWeekEffect = object.getString("thisWeekEffect");
                String nextWeekPlan = object.getString("nextWeekPlan");

                summary = thisWeekComplete;
            }
        }

        if("photo".equals(scenario)){
            String s1 = jsonObject.getString("Keys");
            String deal = s1.substring(1,s1.length()-1);
            String[] arr = deal.split(",");
            String[] arr2 = new String[arr.length];
            String str1 = arr[0];
            String str2 = str1.substring(1,str1.length()-1);
//            String str3 = "https://dongmi3.oss-cn-beijing.aliyuncs.com/"+str2;
            String str3 = "https://www.yunjiworks.com/pics/"+str2;
//            String str3 = "http://10.8.5.239:8080/pics/" + str2;
            article.setPicUrl(str3);
        }

        logger.error("存入redis前的数据 articlId, param", articleId.toString(), param);
        //存入redis
        JedisUtils.saveKey(articleId.toString(), param);

        //存入mysql数据库:article

        article.setArticleId(articleId);
        article.setUserId(Integer.valueOf(userId));
        article.setUsername(Utils.encodeStr(username));
        article.setTitle(title);
        article.setCircle(circle);
        article.setCreatetime(createDate);
        article.setStarttime(startDate);
        article.setScenario(scenario);
        article.setSummary(summary);
        article.setVisibility(visibility);
        article.setSize("1");
        article.setOrganization(organization);
        article.setHtmlUrl(null);
        article.setMdUrl(null);
        article.setShareVisibility(shareVisibility);

        logger.error("要存入mysql的article信息:" + article);
        int res = articleService.addArticle(article);
        logger.error("数据存入mysql返回值:");
        System.out.println("插入数据库结果：" + res);

        rb.setRetBean(articleId);
        return rb;
    }

}




























