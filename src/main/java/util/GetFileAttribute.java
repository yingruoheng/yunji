package util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetFileAttribute {

    /*获取上传文件大小*/
    public static String printSize(File file){
        long size = file.length();
        if(size<1024){
            return String.valueOf(size)+"B";
        }else {
            size = size/1024;
        }
        if (size<1024){
            return String.valueOf(size)+"KB";
        }else {
            size = size/1024;
        }
        if (size<1024){
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";

        }else{
            return "";
        }
    }

    /*获取文档图片*/
    public static String printPic(String html){
        List<String> srcList = new ArrayList(); //用来存储获取到的图片地址
        Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");//匹配字符串中的img标签
        Matcher matcher = p.matcher(html);
        boolean hasPic = matcher.find();
        if(hasPic == true)//判断是否含有图片
        {
            while(hasPic) //如果含有图片，那么持续进行查找，直到匹配不到
            {
                String group = matcher.group(2);//获取第二个分组的内容，也就是 (.*?)匹配到的
                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");//匹配图片的地址
                Matcher matcher2 = srcText.matcher(group);
                if( matcher2.find() )
                {
                    srcList.add( matcher2.group(3) );//把获取到的图片地址添加到列表中
                }
                hasPic = matcher.find();//判断是否还有img标签
            }

        }else {
            return null;
        }
        return srcList.get(0);
    }

    /*获取title*/
    public static String printTitle(String html) {
        String title;
        if(html.contains("<h1>")){
            title = html.substring(html.indexOf("<h1>")+4,html.indexOf("</h1>"));
            return title.replaceAll("</?[^>]+>","");
        }else if (html.contains("<h2>")){
            title = html.substring(html.indexOf("<h2>")+4,html.indexOf("</h2>"));
            return title.replaceAll("</?[^>]+>","");
        }else if (html.contains("<h3>")){
            title = html.substring(html.indexOf("<h3>")+4,html.indexOf("</h3>"));
            return title.replaceAll("</?[^>]+>","");
        }else {
            return "";
        }
    }

    /*获取摘要*/
    public static String printSum(String html){
//        Matcher slashMatcher = Pattern.compile("p>").matcher(html);
//        int i = 2;
//        int mIdx = 0;
//        while (slashMatcher.find()) {
//            mIdx++;
//            if (mIdx == i) {
//                break;
//            }
//        }
//       String summary = html.substring(html.indexOf("<p")+3,slashMatcher.start()-2);

        String content = html.replaceAll("</?[^>]+>","");
        String text= content.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        String summary;
        if (text.length()<80){
            summary=text;
        }else {
            summary= text.substring(0,80);
        }
        return summary;

    }

    /*获取时间*/
    public static Date printDate() throws ParseException {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s1 = sdf.format(d);
        Date date = sdf.parse(s1);
        return date;
    }
}
