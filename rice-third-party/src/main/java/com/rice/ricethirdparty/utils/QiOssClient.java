package com.rice.ricethirdparty.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/3/29 12:04
 */
@Component
public class QiOssClient
{
    @Value("${oss.accessKey}")
    private String accessKey;

    @Value("${oss.secretKey}")
    private String secretKey;

    @Value("${bucket.ricemall}")
    private String bucket;

    /**
     * 获取上传凭证
     * @return
     */
    public String getUpToken()
    {
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucket);

        return token;
    }

    public void uploadFile(String filePath, String key)
    {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try
        {
            Response response = uploadManager.put(filePath, key, upToken);
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
