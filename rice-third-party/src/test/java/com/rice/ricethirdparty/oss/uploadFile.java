package com.rice.ricethirdparty.oss;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.rice.ricethirdparty.utils.QiOssClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/3/28 19:45
 */
@SpringBootTest
public class uploadFile
{

    @Autowired
    private QiOssClient qiOssClient;

    @Test
    public void testQiOssClient()
    {
        qiOssClient.uploadFile("D:\\图片\\1690686058467.jpg","169.jpg");
    }

    @Test
    public void testFile()
    {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "0Ikc37UfOaUG2rwjv9lqnwzNmn9vRlMLCv0dqwHV";
        String secretKey = "UWno72zQCJUznDTp-K-Z9yUI4T3Ks9u1rGtl7giS";
        String bucket = "ricemall";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\图片\\1690686058467.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "1.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try
        {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        }
        catch (QiniuException ex)
        {
            ex.printStackTrace();
            if (ex.response != null)
            {
                System.err.println(ex.response);
                try
                {
                    String body = ex.response.toString();
                    System.err.println(body);
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

}
