package util;

import com.yunji.model.*;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String joinString(Object... args) {

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<args.length;i++){
            sb.append(args[i]);
        }

        return sb.toString();

    }

    public  static Object getJsonValue(String jsonstr,String key){
        Object value = null;
        if(!StringUtils.isEmpty(jsonstr)){
            JSONObject object = JSONObject.fromObject(jsonstr.trim());
            if(object.containsKey(key)){
                value = object.get(key);
            }

        }
        return value;
    }


    public static Date FormatUTCTime(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format.format(cal.getTime());
        Date newDate = null;
        try{
            newDate = format.parse(date1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return newDate;
    }

    public static String FormatBJTime(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, +8);// after 8 hour
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format.format(cal.getTime());

        return date1;
    }

    public static Date transferTime(String time){
        Date date = null;
        try{
            date = formater.parse(time);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return date;

    }

    public static String encodeStr(String str){
        String deStr = null;
        try {
            deStr = Base64.encodeBase64String(str.getBytes("utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return deStr;
    }

    public static String decodeStr(String str){
        String result = null;
        try {
            result = new String(Base64.decodeBase64(str.getBytes()),"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

//    public static  Article setArticleProperty(Article article){
//        switch(article.getCircle()){
//            case "work":
//                article.setCircle("工作人事");
//                break;
//            case "training":
//                article.setCircle("课业培训");
//                break;
//            case "play":
//                article.setCircle("健身休闲");
//                break;
//            case "share":
//                article.setCircle("家庭分享");
//                break;
//        }
//        switch(article.getScenario()){
//            case "weekly":
//                article.setScenario("写周报");
//                break;
//            case "train":
//                article.setScenario("开课啦");
//                break;
//            case "task":
//                article.setScenario("留作业");
//                break;
//            case "activity":
//                article.setScenario("召集");
//                break;
//            case "photo":
//                article.setScenario("光影");
//                break;
//        }
//        return article;
//    }
//
//    public static Interest setInterestProperty(Interest interest){
//        switch(interest.getCircle()){
//            case "work":
//                interest.setCircle("工作人事");
//                break;
//            case "training":
//                interest.setCircle("课业培训");
//                break;
//            case "play":
//                interest.setCircle("健身休闲");
//                break;
//            case "share":
//                interest.setCircle("家庭分享");
//                break;
//        }
//        switch(interest.getScenario()){
//            case "weekly":
//                interest.setScenario("写周报");
//                break;
//            case "train":
//                interest.setScenario("开课啦");
//                break;
//            case "task":
//                interest.setScenario("留作业");
//                break;
//            case "activity":
//                interest.setScenario("召集");
//                break;
//            case "photo":
//                interest.setScenario("光影");
//                break;
//        }
//        return interest;
//
//
//    }
//
//
//    public static ArticleCategory setCategoryProperty(ArticleCategory category){
//        switch(category.getCircle()){
//            case "work":
//                category.setCircle("工作人事");
//                break;
//            case "training":
//                category.setCircle("课业培训");
//                break;
//            case "play":
//                category.setCircle("健身休闲");
//                break;
//            case "share":
//                category.setCircle("家庭分享");
//                break;
//        }
//        switch(category.getScenario()){
//            case "weekly":
//                category.setScenario("写周报");
//                break;
//            case "train":
//                category.setScenario("开课啦");
//                break;
//            case "task":
//                category.setScenario("留作业");
//                break;
//            case "activity":
//                category.setScenario("召集");
//                break;
//            case "photo":
//                category.setScenario("光影");
//                break;
//        }
//        return category;
//    }


    public static category setProperty(category category){
        switch(category.getCircle()){
            case "work":
                category.setCircle("工作人事");
                break;
            case "training":
                category.setCircle("课业培训");
                break;
            case "play":
                category.setCircle("健身休闲");
                break;
            case "share":
                category.setCircle("家庭分享");
                break;
        }
        switch(category.getScenario()){
            case "weekly":
                category.setScenario("写周报");
                break;
            case "train":
                category.setScenario("开课啦");
                break;
            case "task":
                category.setScenario("留作业");
                break;
            case "activity":
                category.setScenario("召集");
                break;
            case "photo":
                category.setScenario("光影");
                break;
        }
        return category;
    }





    public static void main(String args[]){


    }











}
