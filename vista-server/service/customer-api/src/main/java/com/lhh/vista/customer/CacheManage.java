package com.lhh.vista.customer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个简易的缓存池
 * Created by soap on 2016/12/14.
 */
public class CacheManage {
    private Map<Integer, Map<String, CacheItem>> cachemap;

    public CacheManage() {
        cachemap = new ConcurrentHashMap<>();
    }

    public synchronized void put(Integer type, String key, Object obj) {
        Map<String, CacheItem> cacheList = cachemap.get(type);
        if (cacheList == null) {
            cacheList = new ConcurrentHashMap<>();
            cachemap.put(type, cacheList);
        }

        CacheItem cacheItem = new CacheItem();
        cacheItem.cacheTime = System.currentTimeMillis();
        cacheItem.obj = obj;
        cacheList.put(key, cacheItem);
    }

    public CacheItem get(Integer type, String key) {
        Map<String, CacheItem> cacheList = cachemap.get(type);
        if (cacheList != null) {
            CacheItem cacheItem = cacheList.get(key);
            return cacheItem;
        }
        return null;
    }

    public void remove(Integer type, String key) {
        Map<String, CacheItem> cacheList = cachemap.get(type);
        if (cacheList != null) {
            cacheList.remove(key);
        }
    }

    public static class CacheItem {
        public long cacheTime;
        public Object obj;
    }
}
