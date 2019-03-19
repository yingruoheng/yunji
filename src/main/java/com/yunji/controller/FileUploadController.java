package com.yunji.controller;

import com.alibaba.fastjson.JSON;
import com.yunji.model.Article;
import com.yunji.model.ArticleTag;
import com.yunji.model.Pic;
import com.yunji.model.ReturnBean;
import com.yunji.service.ArticleService;
import com.yunji.service.ArticleTagService;
import com.yunji.service.IdStatusService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.markdown4j.Markdown4jProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import redis.clients.jedis.Jedis;
import util.Conversion;
import util.GetFileAttribute;
import util.UpLoadCOS;
import util.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileUploadController {

    public static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private IdStatusService idStatusService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private  ArticleService articleService;

    @RequestMapping("/fileupload")
    @ResponseBody
    public ReturnBean upload(MultipartFile file, HttpServletRequest request,HttpServletResponse response) throws Exception {

        ReturnBean rb = new ReturnBean();

        Integer userId = Integer.valueOf(request.getParameter("userId"));
        if (userId == null){
            rb.setRetVal(1);
            rb.setRetMsg("userId为空，上传失败！");
        }
        String userName = Utils.encodeStr(request.getParameter("username"));
        System.out.println(userName);
        String organization = request.getParameter("organization");
        String visibility = "1";
        String circle = "知识经验";
        String scenario = "品牌推广";
        Integer articleId = idStatusService.saveStatus(666);
        ArticleTag articleTag = new ArticleTag();

        articleTag.setArticleId(articleId);

        String typeList = request.getParameter("list");
        if (typeList!=null) {
            String[] stringArr = typeList.split(",");
            for (int i = 0; i < stringArr.length; i++) {
                String tagName = "type";
                String tagValue = stringArr[i];
                articleTag.setTagname(tagName);
                articleTag.setTagvalue(tagValue);
                articleTagService.addArticleTag(articleTag);
            }
        }

        String tag = request.getParameter("tag");
        if(tag!=null) {
            Map map = (Map) JSON.parse(tag);
            String tagName = (String) map.get("tagKey");
            String tagValue = (String) map.get("tagValue");
            articleTag.setTagname(tagName);
            articleTag.setTagvalue(tagValue);
            articleTagService.addArticleTag(articleTag);
        }

        CommonsMultipartFile cfile = (CommonsMultipartFile) file;
        DiskFileItem fi = (DiskFileItem) cfile.getFileItem();
        File file1 = fi.getStoreLocation();
        String mdUrl = String.valueOf(UpLoadCOS.mkdCOS(file1, userId, articleId));
        File file2 = Conversion.MH(file1);

        String html = new Markdown4jProcessor().process(file1);
        UpLoadCOS.hlCOS(file2, userId, articleId);

        //Attribute
        String title=request.getParameter("title");
        if (title == null || title.equals(""))  {
            title = GetFileAttribute.printTitle(html);
            if (title == null || title.equals("")) title = file.getOriginalFilename().replaceAll("\\..*$", "");
        }
        String size = GetFileAttribute.printSize(file1);
        if (size.equals("")){
            rb.setRetVal(2);
            rb.setRetMsg("上传文件过大，上传失败！");
            return rb;
        }
        String picUrl = GetFileAttribute.printPic(html);
        String sum = GetFileAttribute.printSum(html);
        String hlUrl = "https://yunji-1255930917.cos-website.ap-beijing.myqcloud.com/" + userId + articleId + ".html";
        Date date = GetFileAttribute.printDate();

        Article article = new Article();
        article.setArticleId(articleId);
        article.setUserId(userId);
        article.setCreatetime(date);
        article.setHtmlUrl(hlUrl);
        article.setMdUrl(mdUrl);
        article.setPicUrl(picUrl);
        article.setSize(size);
        article.setSummary(sum);
        article.setTitle(title);
        article.setUsername(userName);
        article.setOrganization(organization);
        article.setCircle(circle);
        article.setScenario(scenario);
        article.setVisibility(visibility);
        articleService.addArticle(article);

        rb.setRetVal(0);
        rb.setRetBean(articleId);
        return rb;
    }

    @RequestMapping("/mdDetail")
    @ResponseBody
    public ReturnBean getMdDetail(@RequestParam("mdUrl") String mdUrl) throws IOException {
        ReturnBean rb = new ReturnBean();
        StringBuffer sb = new StringBuffer();
        URL url = new URL(mdUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
        String line;
        while ((line = br.readLine())!=null){
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        String html = sb.toString();
        System.out.println(html);
        rb.setRetBean(html);
        return rb;
    }

//    @RequestMapping("/weekly")
//    @ResponseBody
//    private void saveWeekly(@RequestBody String param){
//        Jedis jedis = new Jedis("132.232.169.144",6379);
//        logger.info(param);
//        System.out.println(param);
//        jedis.set("uid5",param);
//
//
//    }
//
//    @RequestMapping("/savePic")
//    @ResponseBody
//    private ReturnBean savePic(@RequestBody String param) throws ParseException {
//        logger.info(param);
//        Jedis jedis = new Jedis("132.232.169.144",6379);
//        ReturnBean rb = new ReturnBean();
//        String articleId = String.valueOf(idStatusService.saveStatus(666));
//
//        JSONObject jsonObject = JSONObject.fromObject(param);
//        jedis.set(articleId,param);
//
//        String userId = jsonObject.getString("userId");
//        String username = jsonObject.getString("username");
//        String title = jsonObject.getString("title");
//        String visibility = jsonObject.getString("checked");
//        String organization = jsonObject.getString("org");
//        Date date = GetFileAttribute.printDate();
//
//        Article article = new Article();
//        article.setArticleId(Integer.valueOf(articleId));
//        article.setUserId(Integer.valueOf(userId));
//        article.setUsername(username);
//        article.setTitle(title);
//        article.setCreatetime(date);
//        article.setScenario("photo");
//        article.setVisibility(visibility);
//        article.setSize("1");
//        articleService.addArticle(article);
//
//        rb.setRetVal(0);
//        rb.setRetBean(articleId);
//        return rb;
//
//    }
//
//    @RequestMapping("/getPic")
//    @ResponseBody
//    private ReturnBean getPic(@RequestParam(value="articleId", required = false) String articleId){
//        logger.info(articleId);
//        ReturnBean rb = new ReturnBean();
//        Jedis jedis = new Jedis("132.232.169.144",6379);
//
//        String str = (jedis.get(articleId));
//        System.out.println(str);
//        JSONObject jsonObject = JSONObject.fromObject(str);
//        String strObj = String.valueOf (jsonObject.get("obj"));
//        JSONObject jObj = JSONObject.fromObject(strObj);
//        Map objMap = (Map)jObj;
//
//        ArrayList<Pic> pics = new ArrayList<>();
//        Iterator<String> iter = objMap.keySet().iterator();
//        while(iter.hasNext()){
//            String key=iter.next();
//            String value = (String) objMap.get(key);
//            Pic pic = new Pic();
//            pic.setContent(value);
//            pic.setUrl(key);
//            System.out.println(key+" "+value);
//            pics.add(pic);
//        }
//        System.out.println(pics);
//        System.out.println("objMap:"+objMap);
//
//
//        Map map = (Map)jsonObject;
//        String s1 =String.valueOf( map.get("Keys"));
//        String deal = s1.substring(1,s1.length()-1);
//        String[] arr = deal.split(",");
//        String[] arr2 = new String[arr.length];
//        for (int i = 0; i<arr.length;i++){
//            String str1 = arr[i];
//            String str2 = str1.substring(1,str1.length()-1);
//            String str3 = "https://yunji-1255930917.cos-website.ap-beijing.myqcloud.com/"+str2;
//            arr2[i] = str3;
//        }
//
//        JSONObject reObj = new JSONObject();
//        reObj.put("pics",arr2);
//        reObj.put("obj",jObj);
//
//        System.out.println(reObj);
//
//        rb.setRetVal(0);
//        rb.setRetBean(pics);
//        return rb;
//    }



}



























