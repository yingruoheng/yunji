package com.yunji.service;

import com.yunji.model.*;

import java.util.List;
import java.util.Map;

/**
 * Created by liujialiang on 2018/9/26
 */
public interface ArticleService {
    int addArticle(Article article);

    List<Article> selectUserArticle(String userId);
    // 周报
    List<Article> selectReport(String userId,String time, List<Tag> userTag);

    // 所有推荐文章
    List<Article> selectByRecommend(String articleId,String pagesize);

    //获取关注文章
    List<Article> selectByInterest(String userId, String articleId,String pagesize);

    //获取日程文章
    List<Article> selectBySchedule(String userId, String starttime,String pagesize);

    List<Article> selectByWebRecommend(String articleId,String pagesize);

    int getArticleStar();

    List<Article> selectReport(List<Tag> userTag,String articleId,String pagesize);

    Integer getUserArticleAmount(String userId);

    Integer deleteArticle(String userId,String articleId);

    List<Article> getUserArticle(String userId,String articleSize,String size);

    Article getDetail(Integer articleId);

    List<Comment> getArticleComment(int articleId);

    int addComment(Comment comment);

    int addInterest(Interest interest);

    //  取消关注
    int deleteInterest(Interest interest);

    Integer getStarCount(Integer articleId);

    Integer getReadCount(Integer articleId);

    Integer getCommentCount(Integer articleId);

    List<Comment> getCommentsById(Integer articleId);

    int getActionTypeById(Integer articleId, String actionType);

    int addAction(Action action);

    List<Action> getReadersIdByarticleId(Map<String, Object> param);

    //根据userid, touserid等信息查找是否已添加关注列表
    Integer getLikerNum(Map<String, Object> param);

    //添加用户的组织关系
    int addOrganization(Organization organization);

    //根据userId获取组织
    List<Organization> getOrganizationByUserId(Integer userId);

    int getAction(Map<String, Object> param);

    int getInterest(Map<String, Object> param);

    List<Action> getAction(Integer articleId);

    List<ArticleCategory> getCategoryList();

}
