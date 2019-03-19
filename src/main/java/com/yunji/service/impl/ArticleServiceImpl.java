package com.yunji.service.impl;

import com.yunji.dao.ArticleMapper;
import com.yunji.model.*;
import com.yunji.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service(value = "articleService")
public class ArticleServiceImpl implements ArticleService {
    private static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleMapper articlemapper;

    @Override
    public int addArticle(Article article) {
        return articlemapper.addArticle(article);
    }

    @Override
    public List<Article> selectUserArticle(String userId) {
        return null;
    }

    @Override
    public List<Article> selectReport(String userId, String time, List<Tag> userTag) {
        logger.info("userId{},time{},userTag{}",userId,time,userTag);
        List<Article> articleList = new ArrayList<>();
        try{
            if(StringUtils.isEmpty(time)){
                logger.error("time is empty for userId: "+userId);
            }else{
                Map<String,Object> param = new HashMap<>();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate today = LocalDate.now();
                LocalDate end = today.minus((Integer.valueOf(time)-1), ChronoUnit.WEEKS).plusDays(1);
                LocalDate start = today.minus((Integer.valueOf(time)), ChronoUnit.WEEKS);
                String startTime = start.format(format);
                String endTime = end.format(format);
                param.put("startTime",startTime);
                param.put("endTime",endTime);
                for(int i=0;i<userTag.size();i++){
                    if(userTag.get(i).getTagKey().equals("organization")){
                        param.put("organization",userTag.get(i).getTagValue());
                        break;
                    }
                }
                logger.info("param{}",param);
                articleList = articlemapper.selectReport(param);
                logger.info("articleList{}:",articleList);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return articleList;
    }

/*
*   微信小程序获取推荐文章
 */
    @Override
    public List<Article> selectByRecommend(String artId,String pagesize) {
        logger.info("pagesize{},articleId{}",artId,pagesize);
        List<Article> articleList = new ArrayList<>();
        HashMap<String,Object> param = new HashMap<>();
        Integer limit = null;
        Integer articleId = null;
        try{
            if(StringUtils.isEmpty(pagesize)){
                logger.error("pagesize is empty");
                return  articleList;
            }else{
                limit = Integer.valueOf(pagesize.trim());
            }
            if(!StringUtils.isEmpty(artId)){
                articleId = Integer.valueOf(artId.trim());
            }
            param.put("limit",limit);
            param.put("articleId",articleId);
            logger.info("param");
            articleList = articlemapper.getRecommendArticle(param);
        }catch (Exception e){
            e.getMessage();
            logger.error(e.getMessage());
        }

        logger.info("articleId{},articleList{}",artId,articleList);
        return articleList;
    }

/*
*   微信小程序获取关注文章
 */
    @Override
    public List<Article> selectByInterest(String userId, String artId,String pagesize){
        logger.info("userId{},articleId{},pagesize{}",userId,artId, pagesize);
        List<Article> articleList = new ArrayList<>();
        HashMap<String,Object> param = new HashMap<>();
        Integer limit = null;
        Integer articleId = null;
        try{
            limit = Integer.valueOf(pagesize.trim());
            if(!StringUtils.isEmpty(artId)){
                articleId = Integer.valueOf(artId.trim());
            }
            param.put("limit",limit);
            param.put("userId", userId);
            param.put("articleId",articleId);
            logger.info("param");
            articleList = articlemapper.getInterestArticle(param);
        }catch (Exception e){
            e.getMessage();
            logger.error(e.getMessage());
        }

        logger.info("articleId{},articleList{}",artId,articleList);
        return articleList;
    }

/*
 *   微信小程序获取日程文章
 */
    @Override
    public List<Article> selectBySchedule(String userId, String starttime,String pagesize){
        logger.info("userId{},articleId{},pagesize{}",userId,starttime, pagesize);
        List<Article> articleList = new ArrayList<>();
        HashMap<String,Object> param = new HashMap<>();
        Integer limit = null;
        Integer articleId = null;
        try{
            limit = Integer.valueOf(pagesize.trim());
//            if(!StringUtils.isEmpty(starttime)){
//                articleId = Integer.valueOf(starttime.trim());
//            }
            param.put("limit",limit);
            param.put("userId", userId);
            param.put("starttime",starttime);
            logger.info("param");
            articleList = articlemapper.getScheduleArticle(param);
        }catch (Exception e){
            e.getMessage();
            logger.error(e.getMessage());
        }

        logger.info("articleId{},articleList{}",starttime,articleList);
        return articleList;
    }

    /*
    *网站获取推荐文章
     */
    @Override
    public List<Article> selectByWebRecommend(String artId,String pagesize) {
        logger.info("pagesize{},articleId{}",artId,pagesize);
        List<Article> articleList = new ArrayList<>();
        HashMap<String,Object> param = new HashMap<>();
        Integer limit = null;
        Integer articleId = null;
        try{
            if(StringUtils.isEmpty(pagesize)){
                logger.error("pagesize is empty");
                return  articleList;
            }else{
                limit = Integer.valueOf(pagesize.trim());
            }
            if(!StringUtils.isEmpty(artId)){
                articleId = Integer.valueOf(artId.trim());
            }
            param.put("limit",limit);
            param.put("articleId",articleId);
            logger.info("param");
            articleList = articlemapper.getWebRecArticle(param);
        }catch (Exception e){
            e.getMessage();
            logger.error(e.getMessage());
        }

        logger.info("articleId{},articleList{}",artId,articleList);
        return articleList;
    }

    @Override
    public int getArticleStar() {
//        logger.info("pagesize{},articleId{}",artId,pagesize);
//        List<Article> articleList = new ArrayList<>();
//        Integer articleId = null;
          int s1=0, s2 = 0;
        try{
            s1 = articlemapper.getArticleStar();
        }catch (Exception e){
            e.getMessage();
            logger.error(e.getMessage());
        }

//        logger.info("articleId{},articleList{}",artId,articleList);
        return s1;
    }

    @Override
    public List<Article> selectReport(List<Tag> userTag, String artId, String pagesize) {
        logger.info("articleId{},pagesize{},userTag{}",artId,pagesize,userTag);
        List<Article> articleList = new ArrayList<>();
        Integer limit = null;
        Integer articleId = null;

        try{
            Map<String,Object> param = new HashMap<>();
            for(int i=0;i<userTag.size();i++){
                if(userTag.get(i).getTagKey().equals("organization")){
                    param.put("organization",userTag.get(i).getTagValue());
                    break;
                }
            }
            if(StringUtils.isEmpty(pagesize)){
                logger.error("pagesize is empty");
                return  articleList;
            }else{
                limit = Integer.valueOf(pagesize.trim());
            }
            if(!StringUtils.isEmpty(artId)){
                articleId = Integer.valueOf(artId.trim());
            }
            param.put("limit",limit);
            param.put("articleId",articleId);
            articleList = articlemapper.selectReport(param);
            logger.info("articleList{}:",articleList);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return articleList;
    }



    @Override
    public Integer getUserArticleAmount(String userId) {
        Integer total = null;
        if(!StringUtils.isEmpty(userId)){
            total = articlemapper.getArticleAmount(Integer.valueOf(userId));
        }
        return total;
    }

    @Override
    @Transactional
    public Integer deleteArticle(String userId, String articleId) {
        Map<String, Object> param = new HashMap<>();
        Integer result = null;
        param.put("userId", Integer.valueOf(userId));
        param.put("articleId", Integer.valueOf(articleId));
        result = articlemapper.deleteArticle(param);
        if(result == 1){
            Integer delAct = articlemapper.deleteArticleAction(Integer.valueOf(articleId));
            if(delAct >= 0){
                Integer delCom = articlemapper.deleteArticleComment(Integer.valueOf(articleId));
                if(delCom >= 0){
                    result = 1;
                }
            }
        }

        return result;
    }

    //删除文章点赞，评论，阅读
    public Integer deleteArticleAction(String articleId){


        return null;


    }

    @Override
    public List<Article> getUserArticle(String userId, String articleId, String size) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("articleId", articleId);
        param.put("limit", Integer.valueOf(size));
        List<Article> userArticles = new ArrayList<>();
        userArticles = articlemapper.getUserArticle(param);
        if(userArticles.size()>0){
            for(int i=0;i<userArticles.size();i++){
                Article article = userArticles.get(i);
                article.setUsername(Utils.decodeStr(article.getUsername()));
            }
        }
        return userArticles;
    }

    @Override
    public Article getDetail(Integer articleId) {
        Article article = articlemapper.getDetail(articleId);
        return article;
    }

    @Override
    public List<Comment> getArticleComment(int articleId){
        logger.info("articleId{}",articleId);
        List<Comment> commentList = new ArrayList<>();
        try{
           commentList = articlemapper.getArticleComment(articleId);
        }catch (Exception e){
            e.getMessage();
            logger.error(e.getMessage());
        }
        return commentList;
    }

    /*

     */
    @Override
    public int addComment(Comment comment) {
        System.out.println("comment"+comment.toString());
        int res1 = articlemapper.addComment(comment);
        if(res1 == 1){
            return 1;
        }
        System.out.println("result"+res1);

        return 0;
    }

    @Override
    public int addInterest(Interest interest) {
        int res = articlemapper.addInterest(interest);
        return res;
    }

    @Override
    public int deleteInterest(Interest interest) {
        int res = articlemapper.deleteInterest(interest);
        return res;
    }

    @Override
    public Integer getStarCount(Integer articleId) {
        Integer count = null;
        count = articlemapper.getArticleStarCount(articleId);

        return count;
    }

    @Override
    public Integer getReadCount(Integer articleId) {
        Integer count = null;
        count = articlemapper.getArticleReadCount(articleId);

        return count;


    }

    @Override
    public Integer getCommentCount(Integer articleId) {
        Integer count = null;
        count = articlemapper.getArticleCommentCount(articleId);

        return count;
    }

    @Override
    public List<Comment> getCommentsById(Integer articleId) {
        List<Comment> comments = articlemapper.getCommentsById(articleId);
        return comments;
    }

    @Override
    public int getActionTypeById(Integer articleId, String actionType) {
        int likeNum = articlemapper.getActionTypeById(articleId, actionType);
        return likeNum;
    }

    @Override
    public int addAction(Action action) {
        return articlemapper.addAction(action);
    }

    @Override
    public List<Action> getReadersIdByarticleId(Map<String, Object> param) {
        List<Action> actions = articlemapper.getReadersIdByarticleId(param);
        return actions;
    }

    @Override
    public Integer getLikerNum(Map<String, Object> param) {
        int likerNum = articlemapper.getLikerNum(param);
        return likerNum;
    }

    @Override
    public int addOrganization(Organization organization) {
        return articlemapper.addOrganization(organization);
    }

    @Override
    public List<Organization> getOrganizationByUserId(Integer userId) {
        List<Organization> organizations = articlemapper.getOrganizationByUserId(userId);
        return organizations;
    }

    @Override
    public int getAction(Map<String, Object> param) {
        int result = articlemapper.getAction(param);
        return result;
    }

    @Override
    public int getInterest(Map<String, Object> param) {
        int result = articlemapper.getInterest(param);
        return result;
    }


    public static void main(String args[]) {
        String time = "2";
        LocalDate today = LocalDate.now();
        LocalDate start = today.minus((Integer.valueOf(time) - 1), ChronoUnit.WEEKS);
        LocalDate end = today.minus((Integer.valueOf(time)), ChronoUnit.WEEKS);
        LocalDate test = today.plusDays(1);
        System.out.println("test" + test);
        System.out.println(today);
        System.out.println(start);
        System.out.println(end);
//        String regex = "^1[0-9]{6}[1-9]$";
        String regex = "^[1-9][0-9]*$";
        String userId = "-1";
        if (userId.matches(regex)) {
            System.out.println("合法");
        } else {
            System.out.println("不合法");
        }
    }

    @Override
    public List<Action> getAction(Integer articleId){
        List<Action> actions = new ArrayList<>();
        Map<String,Object> param = new HashMap<>();
        param.put("articleId",articleId);
        actions = articlemapper.getArticleAction(param);
        return actions;

    }

    public List<ArticleCategory> getCategoryList(){
        List<ArticleCategory> categoryList = new ArrayList<>();
        List<String>circles = new ArrayList<>();
        List<ArticleCategory> categories = articlemapper.getArticleCategory();
        if(categories !=null){
            for(ArticleCategory category:categories){
                if(!circles.contains(category.getCircle())){
                    circles.add(category.getCircle());
                    Utils.setProperty(category);
                    categoryList.add(category);
                }
            }
        }
        return categoryList;
    }




}
