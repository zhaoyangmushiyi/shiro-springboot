package com.monochrome.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author monochrome
 * @date 2022/10/30
 */
public class EhCacheTest {

    @Test
    public void test() {
        //获取编译目录下的资源的流对象
        InputStream input = this.getClass().getResourceAsStream("/ehcache.xml"); //获取 EhCache 的缓存管理对象
        CacheManager cacheManager = new CacheManager(input); //获取缓存对象
        Cache cache = cacheManager.getCache("HelloWorldCache"); //创建缓存数据
        Element element = new Element("name","zhang3"); //存入缓存
        cache.put(element);
        //从缓存中取出
        Element element1 = cache.get("name");
        System.out.println(element1.getObjectValue());
    }
}
