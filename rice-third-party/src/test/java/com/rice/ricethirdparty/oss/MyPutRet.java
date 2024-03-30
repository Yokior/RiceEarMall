package com.rice.ricethirdparty.oss;

class MyPutRet
{
    public String key; // 文件保存的 key
    public String hash; // 文件保存的 Etag
    public String bucket; // 文件保存的 bucket
    public long fsize; // 文件的大小，单位：B
}