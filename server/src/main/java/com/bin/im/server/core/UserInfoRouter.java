package com.bin.im.server.core;

import com.bin.im.common.json.JsonHelper;
import com.bin.im.server.cache.CacheInternal;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.spi.ImEngine;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoRouter extends BaseHandler{



    public UserInfoRouter(ImEngine imEngine) {
        super(imEngine);
    }

    private CacheInternal getCache() {
      return  imEngine.getCacheManager().getCache();
    }


    public  LoginInfo addUserLoginInfo(long uid, LoginInfo loginInfo){
        String key = String.valueOf(uid);
        CacheInternal cache = getCache();

        // 获取uid的登录情况  device -> loginInfo
        Map<String, String> allDevice = cache.hgetAll(key);
        LoginInfo oldLoginInfo = null;

        if (allDevice != null && !allDevice.isEmpty()) {

            String device = allDevice.values().iterator().next();

            oldLoginInfo = JsonHelper.fromJson(device, LoginInfo.class);

            cache.del(key);
        }

        cache.hset(key,
                String.valueOf(loginInfo.getCliType()),
                loginInfo.toJson());


        cache.close();
        return oldLoginInfo;
    }


    public  LoginInfo fetchUserLoginInfo(long uid) {
        String key = String.valueOf(uid);
        Map<String, String> allDevice = null;
        try(CacheInternal cache = getCache()) {
            allDevice = cache.hgetAll(key);
        }

        LoginInfo loginInfo = null;
        if (allDevice != null && !allDevice.isEmpty()) {
            String device = allDevice.values().iterator().next();

            loginInfo = JsonHelper.fromJson(device, LoginInfo.class);
        }
        return loginInfo;
    }

    public  Map<Long,LoginInfo> fetchUserLoginInfo(List<Long> vecUid) {
        Map<Long,LoginInfo> loginInfos = new HashMap<>();

        for (int i = 0;i < vecUid.size();i++) {
            Long uid = vecUid.get(i);
            loginInfos.put(uid,fetchUserLoginInfo(uid));
        }
        return loginInfos;
    }


    public  void delUserLoginInfo(long uid,int cliType) {
        String key = String.valueOf(uid);
        try(CacheInternal cache = getCache()) {
            cache.hdel(key,String.valueOf(cliType));
        }
    }

    public  void delUserLoginInfo(long uid) {
        String key = String.valueOf(uid);
        try(CacheInternal cache = getCache()) {
            cache.del(key);
        }
    }



}
