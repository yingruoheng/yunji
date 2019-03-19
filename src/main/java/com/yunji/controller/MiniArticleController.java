package com.yunji.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunji.model.*;
import com.yunji.service.AccountService;
import com.yunji.service.ArticleService;
import com.yunji.service.IUserService;
import com.yunji.service.IdStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import util.COS;
import util.JedisUtils;
import util.Utils;

import java.text.SimpleDateFormat;

import java.util.*;


@Controller
@RequestMapping("/articles")
public class MiniArticleController {
    @Autowired
    private IdStatusService idStatusService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IUserService iUserService;

    /*
    从redis中获取模版明细内容，并根据articleId获取其评论、点赞等
     */
    @RequestMapping("/article")
    @ResponseBody
    public ReturnBean getWeeklyById(@RequestParam(value = "userId", required = false) String userId,
                                    @RequestParam(value = "articleId", required = false) String articleId,
                                    @RequestParam(value = "scenario", required = false) String scenario){
        ReturnBean rb = new ReturnBean();
        System.out.println("articleId:" + articleId);
        String items =JedisUtils.getKey(articleId);
        System.out.println("redis:" + items);
        JSONObject jsonObject = JSONObject.parseObject(items);
        JSONObject object = new JSONObject();
        System.out.println("scenario:" + scenario);
        //这里是文章作者的userid，参数中传的userid为浏览者的userid
        String artUserId = jsonObject.getString("userId");
        if("train".equals(scenario)){
            String address = jsonObject.getString("address");
            System.out.println("address:" + address);
            object.put("address", address);
            JSONArray contents = jsonObject.getJSONArray("contents");
            object.put("contents", contents);
            String startDate = jsonObject.getString("startDate");
            object.put("startDate", startDate);
            String organization = jsonObject.getString("organization");
            object.put("organization", organization);
            String title = jsonObject.getString("title");
            object.put("title", title);
            String createtime = jsonObject.getString("createtime");
            object.put("createtime", createtime);
            String latitude = jsonObject.getString("latitude");
            object.put("latitude", latitude);
            String longitude = jsonObject.getString("longitude");
            object.put("longitude", longitude);
            String duration = jsonObject.getString("duration");
            object.put("duration", duration);
        }
        if("task".equals(scenario)){
            JSONArray contents = jsonObject.getJSONArray("contents");
            object.put("contents", contents);
            String startDate = jsonObject.getString("startDate");
            object.put("startDate", startDate);
            String organization = jsonObject.getString("organization");
            object.put("organization", organization);
            String title = jsonObject.getString("title");
            object.put("title", title);
            String createtime = jsonObject.getString("createtime");
            object.put("createtime", createtime);

            JSONArray pics = jsonObject.getJSONArray("pics");
            ArrayList<String> picList = new ArrayList<>();
            System.out.println("作业照片操作：");
            for(int i=0;i<pics.size();i++){
                String pic = pics.get(i).toString();
                String[] split = pic.split("/");
                String picUrl = "https://www.yunjiworks.com/pics/" + split[split.length - 1];
                System.out.println(picUrl);
                picList.add(picUrl);
            }
            object.put("pics", picList);

            //测试代码段，生产部署时要删掉
//            COS.upload("/Users/fred/Documents/home-btn.png");

        }
        if("activity".equals(scenario)){
            JSONArray contents = jsonObject.getJSONArray("contents");
            object.put("contents", contents);
            String overDate = jsonObject.getString("overDate");
            object.put("overDate", overDate);
            String location = jsonObject.getString("location");
            object.put("location", location);
            String activityName = jsonObject.getString("title");
            object.put("activityName", activityName);
            String introduction = jsonObject.getString("introduction");
            object.put("introduction", introduction);
            String startDate = jsonObject.getString("startDate");
            object.put("startDate", startDate);
            String organization = jsonObject.getString("organization");
            object.put("organization", organization);
            String createtime = jsonObject.getString("createtime");
            object.put("createtime", createtime);
            String latitude = jsonObject.getString("latitude");
            object.put("latitude", latitude);
            String longitude = jsonObject.getString("longitude");
            object.put("longitude", longitude);
            String duration = jsonObject.getString("duration");
            object.put("duration", duration);
        }
        if("weekly".equals(scenario)){
            String createtime = jsonObject.getString("createtime");
            object.put("createtime", createtime);
            JSONArray contents = jsonObject.getJSONArray("contents");
            object.put("contents", contents);
            String weekStartDay = jsonObject.getString("weekStartDay");
            object.put("weekStartDay", weekStartDay);
            String weekEndDay = jsonObject.getString("weekEndDay");
            object.put("weekEndDay", weekEndDay);
            String organization = jsonObject.getString("organization");
            object.put("organization", organization);
            String title = jsonObject.getString("title");
            object.put("title", title);
        }
        if("photo".equals(scenario)){
            UserWxInfo wxInfo = iUserService.getWxinfo(artUserId);
            String organization = jsonObject.getString("organization");
            String title = jsonObject.getString("title");
            object.put("title", title);
            object.put("organization", organization);
            String createtime = jsonObject.getString("createtime");
            object.put("createtime", createtime);
            String strObj = String.valueOf (jsonObject.get("obj"));
            JSONObject jObj = JSONObject.parseObject(strObj);
            Map objMap = (Map)jObj;
            //排序
            Map map = (Map)jsonObject;
            String s1 =String.valueOf( map.get("Keys"));
            System.out.println("照片地址：" + s1);

            String deal = s1.substring(1,s1.length()-1);
            String[] arr = deal.split(",");
            String[] arr2 = new String[arr.length];
            for (int i = 0; i<arr.length;i++){
                String str1 = arr[i];
                String str2 = str1.substring(1,str1.length()-1);
//                String str3 = "https://yunji-1255930917.cos-website.ap-beijing.myqcloud.com/"+str2;
//                String str3 = "http://10.8.5.243:8089/pics/"+str2;
                String str3 = "https://www.yunjiworks.com/pics/"+str2;
//                String str3 = "http://10.8.5.239:8080/pics/"+str2;
                arr2[i] = str3;
            }
            System.out.println("arr2:"+arr2);

            ArrayList<Pic> pics = new ArrayList<>();
            for (int i = 0 ; i<arr2.length;i++){
                String key = arr2[i];
                String value = String.valueOf(objMap.get(key));
                Pic pic = new Pic();
                pic.setContent(value);
                pic.setUrl(key);
                System.out.println(key+" "+value);
                pics.add(pic);

               /* String key=iter.next();
                String value = (String) objMap.get(key);
                Pic pic = new Pic();
                pic.setContent(value);
                pic.setUrl(key);
                System.out.println(key+" "+value);
                pics.add(pic);*/
            }
            System.out.println(pics);
            System.out.println("objMap:"+objMap);

            object.put("pics",pics);
            object.put("wxInfo",wxInfo);
        }


        System.out.println(userId);


        List<Comment> comments = articleService.getCommentsById(Integer.valueOf(articleId));
//        List<Action> actions = articleService.getAction(Integer.valueOf(articleId));
//        for(int i=0;i<actions.size();i++){
//            Comment c = new Comment();
//            c.setArticleId(actions.get(i).getArticleId());
//            c.setUserId(actions.get(i).getUserId());
//            c.setCreatetime(actions.get(i).getCreatetime());
//            if(actions.get(i).getActionType().equals("read")){
//                c.setContent("已阅");
//            }
//            if(actions.get(i).getActionType().equals("like")){
//                c.setContent("点赞");
//            }
//            comments.add(c);
//        }

        //根据articleid获取comment的userid、nickename、头像url等
        List<CommentWxInFo> commentWxInFos = new ArrayList<>();
        for(Comment c:comments){
            int commentUserId = c.getUserId();
            int commentArticleId = c.getArticleId();
            String commentContent = Utils.decodeStr(c.getContent());
            Date commentCreatetime = c.getCreatetime();
            UserWxInfo wxinfo = accountService.getWxinfo(String.valueOf(commentUserId));
            String commentHeadImageUrl = null;
            String commentNickName = null;
            if(null!=wxinfo){
                commentHeadImageUrl = wxinfo.getHeadimageurl();
                commentNickName = wxinfo.getNickname();
            }
            CommentWxInFo commentWxInFo = new CommentWxInFo();
            commentWxInFo.setUserId(commentUserId);
            commentWxInFo.setArticleId(commentArticleId);
            commentWxInFo.setContent(commentContent);
            commentWxInFo.setHeadImageUrl(commentHeadImageUrl);
            commentWxInFo.setNickName(commentNickName);
            commentWxInFo.setCreatetime(commentCreatetime);

            commentWxInFos.add(commentWxInFo);
        }

        //获取点赞数
        int likeNum = articleService.getActionTypeById(Integer.valueOf(articleId), "like");
        System.out.println(likeNum);

        //获取浏览数
        int readNum = articleService.getActionTypeById(Integer.valueOf(articleId), "read");
        System.out.println(readNum);

        //获取评论数
        int commentNum = articleService.getCommentCount(Integer.valueOf(articleId));

        //获取visibility
        String visibility = jsonObject.getString("visibility");
        //获取转发权限shareVisibility
        String shareVisibility = jsonObject.getString("shareVisibility");

        //获取作者的详细信息
        UserWxInfo wxinfo = accountService.getWxinfo(String.valueOf(artUserId));
        String author = null;
        String authorImageUrl = null;
        if(wxinfo != null){
            author = wxinfo.getNickname();
            authorImageUrl = wxinfo.getHeadimageurl();
        }


        //判断用户是否阅读、点赞、参加、评论
        Map<String, Object> readMap = new HashMap<>();
        readMap.put("userId", userId);
        readMap.put("articleId", articleId);
        readMap.put("actionType", "read");
        int isRead = articleService.getAction(readMap);


        Map<String, Object> likeMap = new HashMap<>();
        likeMap.put("userId", userId);
        likeMap.put("articleId", articleId);
        likeMap.put("actionType", "like");
        int isLike = articleService.getAction(likeMap);

//        Map<String, Object> commentMap = new HashMap<>();
//        commentMap.put("userId", userId);
//        commentMap.put("articleId", articleId);
//        commentMap.put("actionType", "comment");
//        int isComment = articleService.getAction(commentMap);

        object.put("visibility", visibility);
        object.put("shareVisibility", shareVisibility);
        //添加了评论者id、nickname、头像url
        object.put("comments", commentWxInFos);
        object.put("articleId", articleId);
        object.put("likeNum", likeNum);
        object.put("readNum", readNum);
        object.put("commentNum", commentNum);
        object.put("userId", artUserId);
        object.put("isRead", isRead);
        object.put("isLike", isLike);
        object.put("author", author);
        object.put("authorImageUrl", authorImageUrl);
//        object.put("isComment", isComment);

        rb.setRetBean(object);
        return rb;
    }

    @RequestMapping("/getComments")
    @ResponseBody
    public ReturnBean getCommentsByArtId(@RequestParam(value = "articleId", required = true) String articleId){
        ReturnBean rb = new ReturnBean();

        JSONObject object = new JSONObject();
        List<Comment> comments = articleService.getCommentsById(Integer.valueOf(articleId));
        //根据articleid获取comment的userid、nickename、头像url等
        List<CommentWxInFo> commentWxInFos = new ArrayList<>();
        for(Comment c:comments){
            int commentUserId = c.getUserId();
            int commentArticleId = c.getArticleId();
            String commentContent = Utils.decodeStr(c.getContent());
            Date commentCreatetime = c.getCreatetime();
            UserWxInfo wxinfo = accountService.getWxinfo(String.valueOf(commentUserId));
            String commentHeadImageUrl = null;
            String commentNickName = null;
            if( wxinfo != null){
                commentHeadImageUrl = wxinfo.getHeadimageurl();
                commentNickName = wxinfo.getNickname();
            }

            CommentWxInFo commentWxInFo = new CommentWxInFo();
            commentWxInFo.setUserId(commentUserId);
            commentWxInFo.setArticleId(commentArticleId);
            commentWxInFo.setContent(commentContent);
            commentWxInFo.setHeadImageUrl(commentHeadImageUrl);
            commentWxInFo.setNickName(commentNickName);
            commentWxInFo.setCreatetime(commentCreatetime);

            commentWxInFos.add(commentWxInFo);
        }

        object.put("comments", commentWxInFos);
        //获取点赞数
        int likeNum = articleService.getActionTypeById(Integer.valueOf(articleId), "like");
        System.out.println(likeNum);

        //获取浏览数
        int readNum = articleService.getActionTypeById(Integer.valueOf(articleId), "read");
        System.out.println(readNum);

        //获取评论数
        int commentNum = articleService.getCommentCount(Integer.valueOf(articleId));

        object.put("likeNum", likeNum);
        object.put("readNum", readNum);
        object.put("commentNum", commentNum);

        rb.setRetBean(object);
        return rb;
    }



    /*
    添加评论
     */
    @RequestMapping("/addComment")
    @ResponseBody
    public ReturnBean addComment(@RequestBody String param){
        ReturnBean rb = new ReturnBean();

        System.out.println(param);

        JSONObject jsonObject = JSONObject.parseObject(param);
        String userId = jsonObject.getString("userId");
        String articleId = jsonObject.getString("articleId");
        String createtime = jsonObject.getString("createtime");
        String content = jsonObject.getString("comment");

        //字符串转时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(createtime);
            date = Utils.FormatUTCTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Comment comment = new Comment();
        comment.setUserId(Integer.valueOf(userId));
        comment.setArticleId(Integer.valueOf(articleId));
        comment.setCreatetime(date);
//        comment.setContent(content);
        comment.setContent(Utils.encodeStr(content));
        System.out.println("Date: " + date);

//        Action action = new Action();
//        action.setCreatetime(date);
//        action.setActionType("comment");
//        action.setArticleId(Integer.valueOf(articleId));
//        action.setUserId(Integer.valueOf(userId));
//        System.out.println("com"+comment.toString());

        int result = articleService.addComment(comment);
        System.out.println("添加评论结果：" + result);
        JSONObject object = new JSONObject();
        object.put("result", "success");
        rb.setRetBean(object);
        return rb;
    }

    /*
    向Action表填加用户操作记录
     */
    @RequestMapping("/addAction")
    @ResponseBody
    public ReturnBean addAction(@RequestBody String param){
        ReturnBean rb = new ReturnBean();

        JSONObject object = new JSONObject();

        System.out.println(param);

        JSONObject jsonObject = JSONObject.parseObject(param);
        String userId = jsonObject.getString("userId");
        String articleId = jsonObject.getString("articleId");
        String actionType = jsonObject.getString("actionType");
        String createtime = jsonObject.getString("createtime");
        String scenario = jsonObject.getString("scenario");

        //判断用户是否阅读、点赞
        Map<String, Object> readMap = new HashMap<>();
        readMap.put("userId", userId);
        readMap.put("articleId", articleId);
        readMap.put("actionType", "read");
        int isRead = articleService.getAction(readMap);
        System.out.println("action中是否已阅:" + isRead);

        Comment comment = new Comment();
        comment.setUserId(Integer.valueOf(userId));
        comment.setArticleId(Integer.valueOf(articleId));
        if(actionType.equals("read") && isRead == 1){
            object.put("result","重复添加");
            rb.setRetBean(object);
            return rb;
        }

        if("read".equals(actionType) && isRead == 0){
            comment.setContent(Utils.encodeStr("已阅"));
        }else if("like".equals(actionType)){
            if("activity".equals(scenario)){
                comment.setContent(Utils.encodeStr("参加"));
            }else if("task".equals(scenario)){
                comment.setContent(Utils.encodeStr("完成"));
            }else if("train".equals(scenario)){
                comment.setContent(Utils.encodeStr("参加"));
            }else if("weekly".equals(scenario)){
                comment.setContent(Utils.encodeStr("批准"));
            }else {
                comment.setContent(Utils.encodeStr("点赞"));
            }
        }

//        if(actionType.equals("read") && isRead == 0){
//            comment.setContent(Utils.encodeStr("已阅"));
//        }else if(actionType.equals("like")){
//            if(scenario.equals("activity")){
//                comment.setContent(Utils.encodeStr("参加"));
//            }else if(scenario.equals("task")){
//                comment.setContent(Utils.encodeStr("完成"));
//            }else if(scenario.equals("train")){
//                comment.setContent(Utils.encodeStr("参加"));
//            }else if(scenario.equals("weekly")){
//                comment.setContent(Utils.encodeStr("批准"));
//            }else {
//                comment.setContent(Utils.encodeStr("点赞"));
//            }
//        }

        //字符串转时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(createtime);
            date = Utils.FormatUTCTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        comment.setCreatetime(date);

        int addCommentResult = articleService.addComment(comment);
        System.out.println("添加comment结果：" + addCommentResult);

        Action action = new Action();
        action.setUserId(Integer.valueOf(userId));
        action.setArticleId(Integer.valueOf(articleId));
        action.setActionType(actionType);
        action.setCreatetime(date);
        System.out.println("Date: " + date);

        int result = articleService.addAction(action);
        System.out.println("添加action结果：" + result);

        if(result == 1){
            object.put("result", "添加成功");
        }else {
            object.put("result", "添加未成功，可能由于重复添加");
        }

        rb.setRetBean(object);
        return rb;
    }

    /*
    根据articleId获取浏览者的id
     */
//    @RequestMapping("/getReadersIdByarticleId")
//    @ResponseBody
//    public ReturnBean getReadersIdByarticleId(@RequestParam(value = "articleId", required = false) String articleId, @RequestParam(value = "actionType", required = false) String actionType){
//        ReturnBean rb = new ReturnBean();
//
//        Map<String, Object> hashMap = new HashMap<>();
//        hashMap.put("articleId", articleId);
//        hashMap.put("actionType", actionType);
//
//        System.out.println("获取浏览者请求：" + articleId);
//        List<Action> readersIdByarticleId = articleService.getReadersIdByarticleId(hashMap);
//
//        System.out.println("浏览者：" + readersIdByarticleId);
//
//        List<UserWxInfo> userWxInfos = new ArrayList<UserWxInfo>();
//
//        for(Action action:readersIdByarticleId){
//            int userId = action.getUserId();
//            UserWxInfo wxinfo = iUserService.getWxinfo(String.valueOf(userId));
//            System.out.println(wxinfo);
//            userWxInfos.add(wxinfo);
//        }
//
//        System.out.println(userWxInfos);
//
//        rb.setRetBean(userWxInfos);
//        return rb;
//    }

    /*
    向Organization表填加
     */
    @RequestMapping("/addOrganization")
    @ResponseBody
    public ReturnBean addOrganization(@RequestBody String param){
        ReturnBean rb = new ReturnBean();

        System.out.println(param);

        JSONObject jsonObject = JSONObject.parseObject(param);
        String userId = jsonObject.getString("userId");
        String organization = jsonObject.getString("organization");

        Organization organ = new Organization();
        organ.setUserId(Integer.parseInt(userId));
        organ.setOrganization(organization);
        int result = articleService.addOrganization(organ);
        System.out.println("添加organization结果：" + result);

        JSONObject object = new JSONObject();

        if(result == 1){
            object.put("result", "添加成功");
        }else {
            object.put("result", "添加未成功，可能由于重复添加");
        }

        rb.setRetBean(object);
        return rb;
    }

    /*
    根据userId获取组织
     */
    @RequestMapping("/getOrganizationByUserId")
    @ResponseBody
    public ReturnBean getOrganizationByUserId(@RequestParam(value = "userId") String userId){
        ReturnBean rb = new ReturnBean();

        List<Organization> organizations = articleService.getOrganizationByUserId(Integer.parseInt(userId));

        System.out.println("组织：" + organizations);

        List<String> organs = new ArrayList<>();

        for(Organization o:organizations){
            organs.add(o.getOrganization());
        }

        rb.setRetBean(organs);
        return rb;
    }

    /*
    判断用户是否已关注
     */
    @RequestMapping("/getInterest")
    @ResponseBody
    public ReturnBean getInterest(@RequestParam(value = "userId") String userId, @RequestParam(value = "toUserId") String toUserId, @RequestParam(value = "scenario") String scenario){
        ReturnBean rb = new ReturnBean();

        System.out.println("判断是否参加：" + userId + "toUserId:" + toUserId + "scenario:" + scenario);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", Integer.valueOf(userId));
        map.put("toUserId", Integer.valueOf(toUserId));
        map.put("scenario", scenario);
        int isInterest = articleService.getInterest(map);
        System.out.println("判断是否参加结果：" + isInterest);

        rb.setRetBean(isInterest);
        return rb;
    }

}











