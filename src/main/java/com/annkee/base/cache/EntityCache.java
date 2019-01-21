package com.annkee.base.cache;

import lombok.Data;

/**
 * @author wangan
 * @date 2018/11/8
 */
@Data
public class EntityCache {
    
    /**
     * 保存的数据
     */
    private Object data;
    
    /**
     * 设置数据失效时间,为0表示永不失效
     */
    private long timeOut;
    
    /**
     * 最后刷新时间
     */
    private long lastRefreshTime;
    
    public EntityCache(Object data, long timeOut, long lastRefreshTime) {
        this.data = data;
        this.timeOut = timeOut;
        this.lastRefreshTime = lastRefreshTime;
    }
    
    
}
