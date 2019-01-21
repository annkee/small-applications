package com.annkee.base.cache;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangan
 * @date 2018/11/8
 */
@Service
public class CacheManagerServiceImpl implements CacheManagerService {
    
    private static Map<String, EntityCache> caches = new ConcurrentHashMap<String, EntityCache>();
    
    /**
     * 存入缓存
     *
     * @param key
     * @param cache
     */
    @Override
    public void putCache(String key, EntityCache cache) {
        caches.put(key, cache);
    }
    
    /**
     * 存入缓存
     *
     * @param key
     * @param data
     */
    @Override
    public void putCache(String key, Object data, long timeOut) {
        timeOut = timeOut > 0 ? timeOut : 10 * 12 * 30 * 24 * 60 * 60 * 1000L;
        putCache(key, new EntityCache(data, timeOut, System.currentTimeMillis()));
    }
    
    /**
     * 获取对应缓存
     *
     * @param key
     * @return
     */
    @Override
    public EntityCache getCacheByKey(String key) {
        if (this.isContains(key)) {
            return caches.get(key);
        }
        return null;
    }
    
    /**
     * 获取对应缓存
     *
     * @param key
     * @return
     */
    @Override
    public Object getCacheDataByKey(String key) {
        if (this.isContains(key)) {
            return caches.get(key).getData();
        }
        return null;
    }
    
    /**
     * 获取所有缓存
     *
     * @return
     */
    @Override
    public Map<String, EntityCache> getCacheAll() {
        return caches;
    }
    
    /**
     * 判断是否在缓存中
     *
     * @param key
     * @return
     */
    @Override
    public boolean isContains(String key) {
        return caches.containsKey(key);
    }
    
    /**
     * 清除所有缓存
     */
    @Override
    public void clearAll() {
        caches.clear();
    }
    
    /**
     * 清除对应缓存
     *
     * @param key
     */
    @Override
    public void clearByKey(String key) {
        if (this.isContains(key)) {
            caches.remove(key);
        }
    }
    
    /**
     * 缓存是否超时失效
     *
     * @param key
     * @return
     */
    @Override
    public boolean isTimeOut(String key) {
        if (!caches.containsKey(key)) {
            return true;
        }
        EntityCache cache = caches.get(key);
        long timeOut = cache.getTimeOut();
        long lastRefreshTime = cache.getLastRefreshTime();
        if (timeOut == 0 || System.currentTimeMillis() - lastRefreshTime >= timeOut) {
            return true;
        }
        return false;
    }
    
    /**
     * 获取所有key
     *
     * @return
     */
    @Override
    public Set<String> getAllKeys() {
        return caches.keySet();
    }
    
}
