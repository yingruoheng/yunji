package util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class COS {
    static String bucketName = "yunji"; //桶的名称
    static String key = "/uploads.png";         //上传到云上路径
    static String region = "ap-beijing";//区域北京则  beijing
    static String appId = "1255930917"; //APPID
    static COSCredentials cred = null;
    static TransferManager transferManager = null;
    static COSClient cosClient = null;

    //生产环境部署时要删掉
    static String filePath = "/Users/fred/Documents/home-btn.png";

    static {
        // 1 初始化用户身份信息(secretId, secretKey)
        //SecretId 是用于标识 API 调用者的身份
        String accessKey = "AKIDnMLhBi07vnWWcmoqeXUlBeEtYHzoF3Hl";
        //SecretKey是用于加密签名字符串和服务器端验证签名字符串的密钥
        String secretKey = "wzMA58GUexU8tz7JVXWXTIA7LjMwY0WA";
        cred = new BasicCOSCredentials(accessKey, secretKey);

        // 2 设置bucket的区域,
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        cosClient = new COSClient(cred, clientConfig);
        // 指定要上传到 COS 上的路径
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池, 默认 TransferManager 中会生成一个单线程的线程池。
        transferManager = new TransferManager(cosClient, threadPool);
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");

    /**
     * 上传
     */
    public static void upload(String filePath) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("上传开始时间:" + sdf.format(new Date()));
                    // .....(提交上传下载请求, 如下文所属)
                    // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
                    String bucket = bucketName + "-" + appId;
                    //本地文件路径

                    File localFile = new File(filePath);
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, localFile);
                    // 本地文件上传
                    Upload upload = transferManager.upload(putObjectRequest);
                    // 异步（如果想等待传输结束，则调用 waitForUploadResult）
                    //UploadResult uploadResult = upload.waitForUploadResult();
                    //同步的等待上传结束waitForCompletion
                    upload.waitForCompletion();
                    System.out.println("上传结束时间:" + sdf.format(new Date()));
                    System.out.println("上传成功");
                    //获取上传成功之后文件的下载地址
                    URL url = cosClient.generatePresignedUrl(bucketName + "-" + appId, key, new Date(new Date().getTime() + 5 * 60 * 10000));
                    System.out.println(url);
                } catch (Throwable tb) {
                    System.out.println("上传失败");
                    tb.printStackTrace();
                } finally {
                    // 关闭 TransferManger
                    transferManager.shutdownNow();
                }
            }
        }).start();

    }

}
