package com.rice.product;

import com.rice.product.entity.SkuInfoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yokior
 * @description
 * @date 2024/5/23 12:55
 */
@SpringBootTest
public class ThreadTest
{
    @Autowired
    private ThreadPoolExecutor executor;

    @Test
    public void testThreadPool()
    {
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() ->
        {
            // sku基本信息获取
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            skuInfoEntity.setSkuId(123L);
            skuInfoEntity.setSkuName("测试商品");
            skuInfoEntity.setPrice(BigDecimal.valueOf(100.00));
            System.out.println("准备返回sku基本信息");
            System.out.println(Thread.currentThread().getId() + "  " +Thread.currentThread().getName());
            System.out.println("==================================================");
            return skuInfoEntity;
        }, executor);


        CompletableFuture<Void> saleFuture = infoFuture.thenAcceptAsync((res) ->
        {
            // 获取spu的销售属性组合
            Long skuId = res.getSkuId();
            System.out.println("获取到skuId：" + skuId);
            System.out.println("获取到spu销售属性");
            System.out.println(Thread.currentThread().getId() + "  " +Thread.currentThread().getName());
            System.out.println("==================================================");
            // ...
        }, executor);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) ->
        {
            // 获取spu的介绍信息
            Long skuId = res.getSkuId();
            System.out.println("获取到skuId：" + skuId);
            System.out.println("获取到spu介绍信息");
            System.out.println(Thread.currentThread().getId() + "  " +Thread.currentThread().getName());
            System.out.println("==================================================");
            // ...
        }, executor);

        CompletableFuture<Void> baseFuture = infoFuture.thenAcceptAsync((res) ->
        {
            // 获取spu的规格信息
            Long skuId = res.getSkuId();
            System.out.println("获取到skuId：" + skuId);
            System.out.println("获取到spu规格信息");
            System.out.println(Thread.currentThread().getId() + "  " +Thread.currentThread().getName());
            System.out.println("==================================================");
            // ...
        }, executor);


        CompletableFuture<Void> imgFuture = CompletableFuture.runAsync(() ->
        {
            // 获取sku的图片信息
            System.out.println("获取到sku图片信息");
            System.out.println(Thread.currentThread().getId() + "  " +Thread.currentThread().getName());
            System.out.println("==================================================");
        }, executor);


        // 等待所有任务完成
        CompletableFuture.allOf(infoFuture, saleFuture, descFuture, baseFuture, imgFuture).join();
        System.out.println("所有任务完成");
        System.out.println(Thread.currentThread().getId() + "  " +Thread.currentThread().getName());
        System.out.println("==================================================");

    }
}
