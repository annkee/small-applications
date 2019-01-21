package com.annkee.base.cache;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangan
 * @date 2018/11/8
 */
@Slf4j
public class CacheListener {
    
    private CacheManagerServiceImpl cacheManagerService;
    
    public CacheListener(CacheManagerServiceImpl cacheManagerImpl) {
        this.cacheManagerService = cacheManagerImpl;
    }
    
    public void startListen() {
        new Thread() {
            @Override
            public void run() {
                for (String key : cacheManagerService.getAllKeys()) {
                    if (cacheManagerService.isTimeOut(key)) {
                        cacheManagerService.clearByKey(key);
                        log.warn(key + "缓存被清除");
                    }
                }
            }
        }.start();
        
    }
}
