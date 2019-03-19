package com.yunji.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


@Controller
@RequestMapping("/pic")
public class PicUploadController {

    private Logger logger = Logger.getLogger(PicUploadController.class);

    /**
     * @param request
     * @param file
     * @return 上传成功返回“success”，上传失败返回“error”
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/upload")
    public String upload(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        System.out.println("执行upload");
        request.setCharacterEncoding("UTF-8");
        logger.info("执行图片上传");
        String user = request.getParameter("user");
        logger.info("user:" + user);
        if (!file.isEmpty()) {
            logger.info("成功获取照片");
            System.out.println("成功获取照片");
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = null;
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            logger.info("图片初始名称为：" + fileName + " 类型为：" + type);
            System.out.println("图片初始名称为：" + fileName + " 类型为：" + type);
            if (type != null) {
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase()) || "JPEG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");

                    //我们想要保存图片的路径
                    String basePath = "/root/pics/";
                    // 自定义的文件名称
                    String trueFileName = fileName;
                    // 设置存放图片文件的路径
                    File filePath = new File(basePath + "uploads");
                    if(!filePath.exists()){
                        filePath.mkdirs();
                        System.out.println("创建文件夹成功");
                    }

                    path = basePath + "uploads/" + trueFileName;
                    logger.info("存放图片文件的路径:" + path);
                    System.out.println("存放图片文件的路径:" + path);
                    file.transferTo(new File(path));
                    logger.info("文件成功上传到指定目录下");
                    System.out.println("文件成功上传到指定目录下");
                } else {
                    logger.info("不是我们想要的文件类型,请按要求重新上传");
                    System.out.println("不是我们想要的文件类型,请按要求重新上传");
                    return "error";
                }
            } else {
                logger.info("文件类型为空");
                System.out.println("文件类型为空");
                return "error";
            }
        } else {
            logger.info("没有找到相对应的文件");
            System.out.println("没有找到相对应的文件");
            return "error";
        }
        return "success";
    }


}
