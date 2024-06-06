package com.bin.im.server.service;

import com.bin.im.common.internal.utils.BiTuple;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.MessageListener;
import com.bin.im.server.event.model.LoginChange;
import com.bin.im.server.repositories.model.user.UserDevTokenModel;
import com.bin.im.server.repositories.model.user.UserLoginModel;
import com.bin.im.server.repositories.sql.user.UserLoginBitMap;

public class UserLoginChangeService extends BaseHandler implements MessageListener<LoginChange> {



    @Override
    public void onMessage(Message<LoginChange> message) {
        LoginChange loginChange = message.getObj();
        logger.info("on login change handler msg:{}",loginChange);
        userLoginChange(loginChange);
    }


    private void userLoginChange(LoginChange loginChange) {
        long uid = loginChange.getUid();
        int sourceType = loginChange.getSourceType();
        String deviceId = loginChange.getDeviceId();
        String clientIp = loginChange.getClientIp();
        String clientVersion = loginChange.getClientVersion();
        long time = loginChange.getTime();
        if (loginChange.isLogin()) {
            UserLoginModel userLogin = new UserLoginModel();
            userLogin.setUid(uid);
            userLogin.setLoginTime(time);
            userLogin.setLogoutTime(0);
            userLogin.setIp(clientIp);
            userLogin.setSourceType(sourceType);

            logger.info("record login data uid:{} sourceType:{} device:{} ", uid,sourceType,deviceId);

            loginReport(userLogin);

            logger.info("set user device device:{} ",deviceId);
            setUserDeviceIdData(userLogin,deviceId,clientVersion);

        } else {
            logger.info("record logout data uid:{} source:{} ",uid,sourceType);

            logoutReport(uid,sourceType,time);
        }
    }


    private void loginReport(UserLoginModel userLogin) {
        long uid = userLogin.getUid();


        logger.info("query user loginmap");

        UserLoginBitMap userLoginBitMap = imDas().queryLoginBitMap(uid).getMsg();//查询用户近一个月的每天登陆情况(登陆位图)


        long loginBitMap = userLoginBitMap.getLoginBitMap();
        int loginDay = userLoginBitMap.getLoginDay();

        int todayBeginTs = getTodayBeginTs();

        int intervalDays = (todayBeginTs - loginDay) / 86400;
        if (intervalDays != 0) {

            loginBitMap = loginBitMap << intervalDays;
            loginBitMap |= 1;

            logger.info("set user loginmap bmp:{} day:{}",loginBitMap,todayBeginTs);

            userLoginBitMap.setUid(uid);
            userLoginBitMap.setLoginBitMap(loginBitMap);
            userLoginBitMap.setLoginDay(todayBeginTs);
            imDas().upsertLoginBitMap(userLoginBitMap);
        } //  == 0 每天只记录一次，已经记录过，不在处理


        logger.info("user real login");
        imDas().upsertLoginData(userLogin);

    }



    //记录用户的登陆设备token
    private void setUserDeviceIdData(UserLoginModel userLogin,String deviceId,String version) {
        UserDevTokenModel userDevToken = new UserDevTokenModel();
        userDevToken.setUid(userLogin.getUid());
        userDevToken.setDevType(userLogin.getSourceType());
        userDevToken.setToken(deviceId);
        userDevToken.setTimestamp(userLogin.getLoginTime());
        userDevToken.setVersion(version);
        imDas().upsertUserDeviceId(userDevToken);
    }


    private void logoutReport(long  uid,int sourceType,long logoutTime) {

        UserLoginModel userLogin = new UserLoginModel();
        userLogin.setUid(uid);
        userLogin.setSourceType(sourceType);
        userLogin.setLogoutTime(logoutTime);
        imDas().upsertLogoutData(userLogin);
    }

    //判断是否需要更新,loginBitMap的一位表示用户一天是否登录
    private int  getTodayBeginTs() {
        long ts = System.currentTimeMillis()/1000;
        return (int)(ts - (ts + 8 * 3600) % 86400);
    }

}
