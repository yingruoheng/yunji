package com.yunji.dao;

//import com.yunji.model.Article;
import com.yunji.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ArticleMapper {

    int addArticle(Article article);

    List<Article> getRecommendArticle(Map<String,Object> param);

    List<Article> getInterestArticle(Map<String,Object> param);

    List<Article> getScheduleArticle(Map<String,Object> param);

    List<Article> getWebRecArticle(Map<String,Object> param);

    List<Article> selectReport(Map<String,Object> param);

    int getArticleStar();

    List<Article> selectAll(String userId);

    Article getDetail(Integer articleId);

    Integer getArticleAmount(Integer userId);

    Integer deleteArticle(Map<String,Object> param);

    Integer deleteAllArticles(String userId);

    //跟下面有重复，需要修改
    List<Comment> getArticleComment(int articleId);

    int addComment(Comment comment);

    int addInterest(Interest interest);

    // 取消关注
    int deleteInterest(Interest interest);

    List<Article> getUserArticle(Map<String,Object> param);

    Integer getArticleStarCount(Integer ariticleId);

    Integer getArticleReadCount(Integer ariticleId);

    Integer getArticleCommentCount(Integer articleId);

    int getActionTypeById(@Param("articleId") Integer articleId,@Param("actionType") String actionType);

    int addAction(Action action);

    //根据文章Id获取评论
    List<Comment> getCommentsById(Integer articleId);

    //从action表删除文章的点赞，阅读，评论
    Integer deleteArticleAction(Integer articleId);

    // 从评论表删除文章评论
    Integer deleteArticleComment(Integer articleId);

    //根据文章Id获取浏览者或评论者的信息
    List<Action> getReadersIdByarticleId(Map<String, Object> param);

    //根据userid, touserid等信息查找是否已添加关注列表
    Integer getLikerNum(Map<String, Object> param);

    //添加用户的组织关系
    int addOrganization(Organization organization);

    //根据userId获取组织
    List<Organization> getOrganizationByUserId(Integer userId);

    //获取用户是否关注、评论、阅读等信息
    int getAction(Map<String, Object> param);

    //
    int getInterest(Map<String, Object> param);

    //判断是否已关注圈子和场景
    int existInterest(Map<String, Object> param);

    List<Action> getArticleAction(Map<String,Object> param) ;

    List<ArticleCategory> getArticleCategory();

}

