package com.yunji.controller;

import com.yunji.model.*;
import com.yunji.service.AccountService;
import com.yunji.service.ArticleService;
import com.yunji.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import util.Utils;
import util.ValidationPatternEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/article")
public class ArticleController {

    public static Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IUserService userService;

    /*
     *   微信小程序获取推荐文章
     */
    @RequestMapping("/articles")
    @ResponseBody
    public ReturnBean getRecommendArticle(@RequestParam(value = "articleId", required = false) String articleId, @RequestParam("pagesize") String pagesize) {
        logger.info("articleId{},pagesize{}", articleId, pagesize);
        ReturnBean returnBean = new ReturnBean();
        List<Article> articleList = new ArrayList<>();
        if (articleId != null) {
            if (!articleId.matches(ValidationPatternEnum.ARTICLEID.getPattern())) {
                returnBean.setRetVal(104);
                returnBean.setRetMsg("articleId格式不对");
                return returnBean;
            }
        }
        if (pagesize != null) {
            if (!pagesize.matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())) {
                returnBean.setRetVal(105);
                returnBean.setRetMsg("pagesize格式不对");
                return returnBean;
            }
        }

        articleList = articleService.selectByRecommend(articleId, pagesize);
        if(articleList.size()>0){
            for(int i=0;i<articleList.size();i++){
                Article article = articleList.get(i);
                article.setDateStr(Utils.FormatBJTime(article.getCreatetime()));
                article.setUsername(Utils.decodeStr(article.getUsername()));
                Integer starNum = articleService.getStarCount(article.getArticleId());
                Integer readNum = articleService.getReadCount(article.getArticleId());
                Integer commentNum = articleService.getCommentCount(article.getArticleId());
                UserWxInfo userWxInfo = accountService.getWxinfo(article.getUserId().toString());
                if ( userWxInfo != null) {
                    article.setHeadImageUrl(userWxInfo.getHeadimageurl());
                }
                article.setReadNum(readNum);
                article.setStarNum(starNum);
                article.setCommentNum(commentNum);
                Utils.setProperty(article);
            }
        }
        returnBean.setRetBean(articleList);
        return returnBean;
    }

    /*
     *   微信小程序获取关注文章
     */
    @RequestMapping("/articlesRecommend")
    @ResponseBody
    public ReturnBean getInterestArticle(@RequestParam(value = "userId", required = true) String userId,
                                                @RequestParam(value = "articleId", required = false) String articleId,
                                                @RequestParam("pagesize") String pagesize) {
        logger.info("userId{}, articleId{},pagesize{}", userId, articleId, pagesize);
        ReturnBean returnBean = new ReturnBean();
        List<Article> articleList = new ArrayList<>();
        if (articleId != null) {
            if (!articleId.matches(ValidationPatternEnum.ARTICLEID.getPattern())) {
                returnBean.setRetVal(104);
                returnBean.setRetMsg("articleId格式不对");
                return returnBean;
            }
        }
        if (!pagesize.matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())) {
            returnBean.setRetVal(105);
            returnBean.setRetMsg("pagesize格式不对");
            return returnBean;
        }

        articleList = articleService.selectByInterest(userId, articleId, pagesize);
        if(articleList.size()>0){
            for(int i=0;i<articleList.size();i++){
                Article article = articleList.get(i);
                article.setDateStr(Utils.FormatBJTime(article.getCreatetime()));
                article.setUsername(Utils.decodeStr(article.getUsername()));
                Integer starNum = articleService.getStarCount(article.getArticleId());
                Integer readNum = articleService.getReadCount(article.getArticleId());
                Integer commentNum = articleService.getCommentCount(article.getArticleId());
                UserWxInfo userWxInfo = accountService.getWxinfo(article.getUserId().toString());
                if ( userWxInfo != null) {
                    article.setHeadImageUrl(userWxInfo.getHeadimageurl());
                }
                article.setStarNum(starNum);
                article.setReadNum(readNum);
                article.setCommentNum(commentNum);
                Utils.setProperty(article);

            }
        }
        returnBean.setRetBean(articleList);
        return returnBean;
    }

    /*
     *   微信小程序获取日程文章
     */
    @RequestMapping("/scheduleArticle")
    @ResponseBody
    public ReturnBean getScheduleArticle(@RequestParam(value = "userId", required = true) String userId,
                                         @RequestParam(value = "starttime", required = false) String starttime,
                                         @RequestParam("pagesize") String pagesize) {
        logger.info("userId{}, articleId{},pagesize{}", userId, starttime, pagesize);
        ReturnBean returnBean = new ReturnBean();
        List<Article> articleList = new ArrayList<>();
//        if (starttime != null) {
//            if (!starttime.matches(ValidationPatternEnum.ARTICLEID.getPattern())) {
//                returnBean.setRetVal(104);
//                returnBean.setRetMsg("articleId格式不对");
//                return returnBean;
//            }
//        }
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//
//        try {
//            startDate = format.parse(starttime);
//            startDate = Utils.FormatUTCTime(startDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (!pagesize.matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())) {
            returnBean.setRetVal(105);
            returnBean.setRetMsg("pagesize格式不对");
            return returnBean;
        }

        if( starttime==null || starttime ==""){
            //字符串转时间格式
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDate = format.format(new Date());
            articleList = articleService.selectBySchedule(userId, startDate, pagesize);
        }
        else {
            articleList = articleService.selectBySchedule(userId, starttime, pagesize);
        }
        if(articleList.size()>0){
            for(int i=0;i<articleList.size();i++){
                Article article = articleList.get(i);
                article.setDateStr(Utils.FormatBJTime(article.getCreatetime()));
                article.setUsername(Utils.decodeStr(article.getUsername()));
                Integer starNum = articleService.getStarCount(article.getArticleId());
                Integer readNum = articleService.getReadCount(article.getArticleId());
                Integer commentNum = articleService.getCommentCount(article.getArticleId());
                UserWxInfo userWxInfo = accountService.getWxinfo(article.getUserId().toString());
                if ( userWxInfo != null) {
                    article.setHeadImageUrl(userWxInfo.getHeadimageurl());
                }
                article.setStarNum(starNum);
                article.setReadNum(readNum);
                article.setCommentNum(commentNum);
                Utils.setProperty(article);

            }
        }
        returnBean.setRetBean(articleList);
        return returnBean;
    }


    /*
     *   网站获取推荐文章
     */
    @RequestMapping("/webRecArticles")
    @ResponseBody
    public ReturnBean getWebRecArticle(@RequestParam(value = "articleId", required = false) String articleId, @RequestParam("pagesize") String pagesize) {
        logger.info("articleId{},pagesize{}", articleId, pagesize);
        ReturnBean returnBean = new ReturnBean();
        List<Article> articleList = new ArrayList<>();
        if (articleId != null) {
            if (!articleId.matches(ValidationPatternEnum.ARTICLEID.getPattern())) {
                returnBean.setRetVal(104);
                returnBean.setRetMsg("articleId格式不对");
                return returnBean;
            }
        }
        if (pagesize != null) {
            if (!pagesize.matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())) {
                returnBean.setRetVal(105);
                returnBean.setRetMsg("pagesize格式不对");
                return returnBean;
            }
        }
        articleList = articleService.selectByWebRecommend(articleId, pagesize);
        if(articleList.size()>0){
            for(int i=0;i<articleList.size();i++){
                Article article = articleList.get(i);
                article.setDateStr(Utils.FormatBJTime(article.getCreatetime()));
                article.setUsername(Utils.decodeStr(article.getUsername()));
                Integer starNum = articleService.getStarCount(article.getArticleId());
                Integer readNum = articleService.getReadCount(article.getArticleId());
                Integer commentNum = articleService.getCommentCount(article.getArticleId());
                UserWxInfo userWxInfo = accountService.getWxinfo(article.getUserId().toString());
                if ( userWxInfo != null) {
                    article.setHeadImageUrl(userWxInfo.getHeadimageurl());
                }
                article.setReadNum(readNum);
                article.setStarNum(starNum);
                article.setCommentNum(commentNum);
                Utils.setProperty(article);
            }
        }
        returnBean.setRetBean(articleList);
        return returnBean;
    }



    @RequestMapping("/report")
    @ResponseBody
    public ReturnBean getReport(@RequestBody String param) {
        ReturnBean returnBean = new ReturnBean();
        List<Article> articleList = new ArrayList<>();
        logger.info(param);
        String userId = null;
        List<Tag> userTag = new ArrayList<Tag>();
        String pagesize = null;
        String articleId = null;
        try {
            JSONObject jsonObject = JSONObject.fromObject(param);
            if (jsonObject.containsKey("articleId")) {
                articleId = (String) jsonObject.get("articleId");
                if (!articleId.matches(ValidationPatternEnum.ARTICLEID.getPattern())) {
                    returnBean.setRetVal(104);
                    returnBean.setRetMsg("articleId格式不对");
                    return returnBean;
                }
            }
            if (jsonObject.containsKey("pagesize")) {
                pagesize = (String) jsonObject.get("pagesize");
                if (!pagesize.matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())) {
                    returnBean.setRetVal(105);
                    returnBean.setRetMsg("pagesize格式不对");
                    return returnBean;
                }
            } else {
                returnBean.setRetVal(107);
                returnBean.setRetMsg("pagesize 为空");
                return returnBean;
            }
            if (jsonObject.containsKey("tagList")) {
                JSONArray array = jsonObject.getJSONArray("tagList");
                if (array.size() != 0) {
                    for (int i = 0; i < array.size(); i++) {
                        Tag tag = new Tag();
                        JSONObject object = (JSONObject) array.get(i);
                        String tagKey = null;
                        String tagValue = null;
                        if (object.containsKey("tagKey")) {
                            tagKey = object.getString("tagKey");
                            tag.setTagKey(tagKey);
                        }
                        if (object.containsKey("tagValue")) {
                            tagValue = object.getString("tagValue");
                            tag.setTagValue(tagValue);
                        }
                        userTag.add(tag);
                    }
                }
            } else {
                returnBean.setRetVal(11);
                returnBean.setRetMsg("用户标签不能为空");
                return returnBean;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("userId,userTag,articleId,pagesize", userId, userTag, articleId, pagesize);
        articleList = articleService.selectReport(userTag, articleId, pagesize);


//        try{
//            JSONObject jsonObject = JSONObject.fromObject(param);
//            if(jsonObject.containsKey("userId")){
//                userId = (String)jsonObject.get("userId");
//            }else{
//                returnBean.setRetVal(1);
//                returnBean.setRetMsg("userId为空");
//                return returnBean;
//            }
//            if(jsonObject.containsKey("time")){
//                time = (String)jsonObject.get("time");
//            }else{
//                returnBean.setRetVal(10);
//                returnBean.setRetMsg("时间不能为空");
//                return returnBean;
//            }
//            JSONArray array = jsonObject.getJSONArray("tagList");
//            if(array.size()!=0){
//                for(int i=0;i<array.size();i++){
//                    Tag tag = new Tag();
//                    JSONObject object = (JSONObject)array.get(i);
//                    String tagKey = null;
//                    String tagValue = null;
//                    if(object.containsKey("tagKey")){
//                        tagKey = object.getString("tagKey");
//                        tag.setTagKey(tagKey);
//                    }
//                    if(object.containsKey("tagValue")){
//                        tagValue = object.getString("tagValue");
//                        tag.setTagValue(tagValue);
//                    }
//                    userTag.add(tag);
//                    logger.info("userId{},time{},userTag{}",userId,time,userTag);
//                }
//            }else{
//                returnBean.setRetVal(11);
//                returnBean.setRetMsg("用户标签不能为空");
//                return returnBean;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error(e.getMessage());
//        }
//        logger.info("userTag{},userId{},time{}",userTag,userId,time);
//        articleList = articleService.selectReport(userId,time,userTag);
        logger.info("articleList{}", articleList);
        returnBean.setRetBean(articleList);
        return returnBean;
    }


    @RequestMapping("/detail")
    @ResponseBody
    public ReturnBean getDetail(@RequestParam("articleId") String param) throws IOException {
        ReturnBean rb = new ReturnBean();
        Integer articleId = Integer.parseInt(param);
        logger.info(param);
        if (articleId == null) {
            rb.setRetVal(22);
            rb.setRetMsg("用户ID为空，获取文章失败！");
        }
        Article article = articleService.getDetail(articleId);
        UserWxInfo wxInfo = accountService.getWxinfo(String.valueOf(article.getUserId()));
        article.setHeadImageUrl(wxInfo.getHeadimageurl());
        article.setUsername(Utils.decodeStr(article.getUsername()));
        StringBuffer sb = new StringBuffer();
        String urlStr = article.getHtmlUrl();
        URL url = new URL(urlStr);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
        String line;
        while ((line = br.readLine())!=null){
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        String html = sb.toString();
        article.setHtmlUrl(html);

        rb.setRetVal(0);
        rb.setRetBean(article);
        return rb;
    }

    @RequestMapping(value = "/amount")
    @ResponseBody
    public ReturnBean getArticleNum(@RequestParam(value = "userId") String userId){
        long time1 = System.currentTimeMillis();
        logger.info("userId{}",userId);
        Map<String,Object> param = new HashMap<>();
        ReturnBean returnBean = new ReturnBean();
        int amount = 0;
        if(!userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
            returnBean.setRetVal(2);
            returnBean.setRetMsg("userId格式不对");
            return returnBean;
        }
        amount = articleService.getUserArticleAmount(userId);
        param.put("amount",amount);
        logger.info("amount{}",amount);
        returnBean.setRetBean(param);
        long time2 = System.currentTimeMillis();
        long time = time2-time1;
        System.out.println(time);
        return returnBean;
    }


    @RequestMapping(value = "/delete")
    @ResponseBody
    public ReturnBean deleteArticle(@RequestParam(value = "articleId") String articleId,@RequestParam(value = "userId") String userId){
        logger.info("userId{},articleId{}",userId,articleId);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        if(!userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
            returnBean.setRetVal(2);
            returnBean.setRetMsg("userId格式不对");
            return returnBean;
        }
        if(!articleId.trim().matches(ValidationPatternEnum.ARTICLEID.getPattern())){
            returnBean.setRetVal(3);
            returnBean.setRetMsg("articleId格式不对");
            return returnBean;
        }
        Integer result = articleService.deleteArticle(userId,articleId);
        if(result == 1){
            param.put("result","删除成功");
        }else{
            param.put("result","删除失败");
        }
        logger.info("param",param);
        returnBean.setRetBean(param);
        return returnBean;
    }

    /*
    * 获取评论
    * */
    @RequestMapping("/comment")
    @ResponseBody
    public ReturnBean getArticleComment(@RequestParam("articleId") int articleId) {
        logger.info("articleId{}", articleId);
        ReturnBean returnBean = new ReturnBean();
        List<Comment> commentList = new ArrayList<>();
        if (articleId != 0) {
            if (!(articleId + "").matches(ValidationPatternEnum.ARTICLEID.getPattern())) {
                returnBean.setRetVal(104);
                returnBean.setRetMsg("articleId格式不对");
                return returnBean;
            }
        }
        commentList = articleService.getArticleComment(articleId);
        returnBean.setRetBean(commentList);
        return returnBean;
    }
    /*
    *
    * 添加评论
    * */
//    @RequestMapping(value="/addComment",method = RequestMethod.POST)
//    @ResponseBody
//    public ReturnBean addComment(@RequestBody String param){
//        logger.info(param);
//        ReturnBean returnBean = new ReturnBean();
//        Map<String, Object> result = new HashMap<>();
//        String userId = String.valueOf(Utils.getJsonValue(param,"userId"));
//        String articleId = String.valueOf(Utils.getJsonValue(param,"articleId"));
////        String createTime = String.valueOf(Utils.getJsonValue(param,"createTime"));
//        String content = String.valueOf(Utils.getJsonValue(param,"content"));
//        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(articleId) || StringUtils.isEmpty(content)){
//            returnBean.setRetVal(100);
//            returnBean.setRetMsg("缺少参数");
//        }else{
//            if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
//                Comment comment = new Comment();
//                comment.setUserId(Integer.valueOf(userId));
//                comment.setArticleId(Integer.valueOf(articleId));
//                comment.setContent(content);
//                logger.info("comment"+comment);
//                int amount = articleService.addComment(comment);
//                if(amount == 1){
//                    result.put("result","添加成功");
//                }else {
//                    result.put("result", "添加失败");
//                }
//                System.out.println(amount);
//            }else{
//
//            }
//        }
//        returnBean.setRetBean(result);
//        return returnBean;
//    }
    /*
    *
    * 关注
    * */
    @RequestMapping(value = "/addInterest", method = RequestMethod.POST)
    @ResponseBody
    public ReturnBean addInterest(@RequestBody String param){
        ReturnBean rb = new ReturnBean();
        com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(param);
        String userId = jsonObject.getString("userId");
        String toUserId = jsonObject.getString("toUserId");
        String circle = jsonObject.getString("circle");
        String scenario = jsonObject.getString("scenario");
        String createtime = jsonObject.getString("createtime");

        System.out.println(createtime);

        //字符串转时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(createtime);
            date = Utils.FormatUTCTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(date);

        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(toUserId) || StringUtils.isEmpty(circle) || StringUtils.isEmpty(scenario)){
            rb.setRetVal(100);
            rb.setRetMsg("缺少参数");
        }else{
            if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
                Interest interest = new Interest();
                interest.setUserId(Integer.valueOf(userId));
                interest.setToUserId(Integer.valueOf(toUserId));
                interest.setCircle(circle);
                interest.setScenario(scenario);
                interest.setCreateTime(date);

                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("userId", Integer.valueOf(userId));
                hashMap.put("toUserId", Integer.valueOf(toUserId));
                hashMap.put("circle", circle);
                hashMap.put("scenario", scenario);

                Integer likerNum = articleService.getLikerNum(hashMap);
                System.out.println("关注：" + likerNum);
                if(likerNum == 0){
                    int amount = articleService.addInterest(interest);
                    System.out.println(amount);
                    if(amount == 1){
                        object.put("result","添加成功");
                    }else {
                        object.put("result", "添加失败");
                    }
                }
                else {
                    object.put("result", "已关注");
                }
            }
        }
        rb.setRetBean(object);
        return rb;
    }

    /*
    *
    * 取消关注
    * */
    @RequestMapping(value = "/deleteInterest", method = RequestMethod.POST)
    @ResponseBody
    public ReturnBean deleteInterest(@RequestBody String param){
        ReturnBean returnBean = new ReturnBean();
        Map<String, Object> result = new HashMap<>();
        String userId = String.valueOf(Utils.getJsonValue(param,"userId"));
        String toUserId = String.valueOf(Utils.getJsonValue(param,"toUserId"));
        String circle = String.valueOf(Utils.getJsonValue(param,"circle"));
        String scenario = String.valueOf(Utils.getJsonValue(param,"scenario"));
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(toUserId) || StringUtils.isEmpty(circle) || StringUtils.isEmpty(scenario)){
            returnBean.setRetVal(100);
            returnBean.setRetMsg("缺少参数");
        }else{
            if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
                Interest interest = new Interest();
                interest.setUserId(Integer.valueOf(userId));
                interest.setToUserId(Integer.valueOf(toUserId));
                interest.setCircle(circle);
                interest.setScenario(scenario);
                int amount = articleService.deleteInterest(interest);
                if(amount == 1){
                    result.put("result","删除成功");
                }else {
                    result.put("result", "删除失败");
                }
                System.out.println(amount);
            }
        }
        returnBean.setRetBean(result);
        return returnBean;
    }

    /*
    *  点赞
    * */
    @RequestMapping("/getArticleStar")
    @ResponseBody
    public ReturnBean getArticleStar(){
        ReturnBean returnBean = new ReturnBean();
        int star = articleService.getArticleStar();
        returnBean.setRetBean(star);
        return returnBean;
    }

    @RequestMapping(value = "/myarticles")
    @ResponseBody
    public ReturnBean getMyArticleList(@RequestParam("userId") String userId,
                                       @RequestParam(value="articleId",required = false) String articleId,
                                       @RequestParam("size") String size){
        logger.info("userId: "+userId);
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        if(StringUtils.isEmpty(userId)){
            returnBean.setRetVal(100);
            returnBean.setRetMsg("userId is empty");
        }else{
            if(userId.trim().matches(ValidationPatternEnum.USERID.getPattern())){
                if(!StringUtils.isEmpty(articleId)){
                    if(!articleId.trim().matches(ValidationPatternEnum.ARTICLEID.getPattern())){
                        returnBean.setRetVal(101);
                        returnBean.setRetMsg("articleId is wrong pattern");
                        return returnBean;
                    }
                }
                if(!StringUtils.isEmpty(size)){
                    if(!size.trim().matches(ValidationPatternEnum.POSITIVE_INTEGER.getPattern())){
                        returnBean.setRetVal(101);
                        returnBean.setRetMsg("size is wrong pattern");
                        return returnBean;
                    }
                }
                List<Article> articleList = new ArrayList<>();
                articleList = articleService.getUserArticle(userId,articleId,size);
                if(articleList.size()>0){
                    for(int i=0;i<articleList.size();i++){
                        Article article = articleList.get(i);
                        article.setDateStr(Utils.FormatBJTime(article.getCreatetime()));
                        Integer starNum = articleService.getStarCount(article.getArticleId());
                        Integer readNum = articleService.getReadCount(article.getArticleId());
                        Integer commentNum = articleService.getCommentCount(article.getArticleId());
                        article.setStarNum(starNum);
                        article.setReadNum(readNum);
                        article.setCommentNum(commentNum);
                        Utils.setProperty(article);
                    }
                }
                param.put("articleList",articleList);
                returnBean.setRetBean(param);
            }else{
                returnBean.setRetVal(101);
                returnBean.setRetMsg("userId is wrong pattern");
            }
        }

        return returnBean;

    }

    /*
    * 获取文章的已阅、点赞、评论数
    * */
    @RequestMapping("/actionNum")
    @ResponseBody
    public ReturnBean getArticleAction(@RequestParam("articleId") String articleId){
        ReturnBean returnBean = new ReturnBean();
        Map<String,Object> param = new HashMap<>();
        Integer starCount = 0;
        Integer readCount = 0;
        Integer commentCount = 0;
        if(!StringUtils.isEmpty(articleId)){
            if(articleId.matches(ValidationPatternEnum.ARTICLEID.getPattern())){
                starCount = articleService.getStarCount(Integer.valueOf(articleId));
                readCount = articleService.getReadCount(Integer.valueOf(articleId));
                commentCount = articleService.getCommentCount(Integer.valueOf(articleId));
                param.put("starCount",starCount);
                param.put("readCount",readCount);
                param.put("commentCount",commentCount);
                returnBean.setRetBean(param);

            }else{

            }

        }else{
            returnBean.setRetVal(100);
            returnBean.setRetMsg("userId is wrong pattern");
        }
        return returnBean;

    }

    /**
     * 获取所有的圈子场景
     */
    @RequestMapping(value="/circle",method = RequestMethod.GET)
    @ResponseBody
    public ReturnBean getArticleCategory(){
        ReturnBean returnBean = new ReturnBean();
        List<ArticleCategory> categories = articleService.getCategoryList();
        returnBean.setRetBean(categories);
        return returnBean;
    }

}








