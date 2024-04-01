package com.rice.ricethirdparty.Controller;

import com.rice.common.utils.R;
import com.rice.ricethirdparty.utils.QiOssClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/3/30 15:46
 */
@RestController
@RequestMapping("/third-party/file")
public class FileController
{
    @Autowired
    private QiOssClient qiOssClient;

    @GetMapping("/getUploadToken")
    public R getUploadToken()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("uploadToken", qiOssClient.getUpToken());
        return R.ok().put("data", map);
    }
}
