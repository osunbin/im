package com.bin.im.server.core.user;


import com.bin.im.common.internal.utils.StringUtils;
import com.bin.im.server.domain.UserLogModel;
import com.bin.im.server.domain.UserLogResultModel;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.domain.KickoutUserNotify;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.model.LoginChange;
import com.bin.im.server.spi.impl.ServerContext;
import com.bin.im.server.spi.impl.ServiceManager;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.bin.im.common.GlobalCodes.RESP_CLIENT_ERROR;
import static com.bin.im.common.GlobalCodes.USER_LOGIN_RESP_COOKIE_ERROR;
import static com.bin.im.server.domain.KickoutUserNotify.KICKOUT_REASON_PC_TO_MOBILE;
import static com.bin.im.server.event.EventService.LOGIN_CHANGE_PUSH_TOPIC;

/**
 *   *  AES(userToken)
 *  *  userToken="UID=49529751616525&UN=rHcCBy&TT=885cfdaa0363283......
 *  *  uid
 *  *  device 设备类型
 *  *  ts   过期时间
 *  *  clientType
 *  实现用户登录业务（包括： 更新缓存、 记录device、 记录登录事件）
 */
public class UserLoginHandler extends BaseHandler {

    public final  static long  INVALID_IM_USERID = 0x00000000;


    // clientIp  mdc
    public UserLogResultModel login(UserLogModel userLog) {
        String clientIp = userLog.getClientIp();


        long uid = userLog.getUid();
        logger.info(" user  login clientVersion={}",  userLog.getClientVersion());
        String userToken = userLog.getUserToken();
        if (StringUtils.isEmpty(userToken)) {
            // TODO 生成 token
            return UserLogResultModel.of(uid,RESP_CLIENT_ERROR);
        }

        long loginUserId = getLoginUserId(userToken);
        if (INVALID_IM_USERID == loginUserId) {
            logger.error(" user login fail userToken={}",userToken);
            return   UserLogResultModel.of(uid,USER_LOGIN_RESP_COOKIE_ERROR);
        }

        logger.info(" user login source={}  clientVersion={} reqUid={}  deviceid={}  userToken={}",
                userLog.getSourceType(),userLog.getClientVersion(),
                userLog.getUid(),userLog.getDeviceId(),
                userLog.getUserToken());




        long currentTime = System.currentTimeMillis();



        int sourceType = userLog.getSourceType();
        String deviceId = userLog.getDeviceId();


        logger.info("login  uid:{} source:{} client version:{} deviceid:{} ",
                uid,sourceType,userLog.getClientVersion(),deviceId);

        ServerContext currContext = ServiceManager.getCurrContext();
        String esIp = currContext.getEsIp();
        int esPort = currContext.getEsPort();

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setLoginTime(currentTime);
        loginInfo.setLoginESIp(esIp);
        loginInfo.setLoginESPort(esPort);
        loginInfo.setCliType(sourceType);
        loginInfo.setKeepaliveTime(0);
        loginInfo.setVersion(userLog.getClientVersion());


        LoginInfo oldLoginInfo =
                getUserInfoRouter().addUserLoginInfo(uid,loginInfo);


        if (loginInfo.isLogined(oldLoginInfo)) {


            KickoutUserNotify kickoutUserNotify =
                    new KickoutUserNotify(uid, currentTime, oldLoginInfo.getCliType(),
                    KICKOUT_REASON_PC_TO_MOBILE, sourceType);

            MsgNotifyInfo msgNotifyInfo =
                    MsgNotifyInfo.create(uid, oldLoginInfo.getLoginESIp(), oldLoginInfo.getLoginESPort());

            getImEngine().kickout(msgNotifyInfo,kickoutUserNotify);

            logger.info("kickout uid:{} oldEsIp:{} newEsIp:{} ",uid, esIp, oldLoginInfo.getLoginESIp());
        }


        LoginChange loginChange =
                LoginChange.ofLogin(uid, sourceType, deviceId,
                        userLog.getClientVersion(), clientIp,currentTime);
        publishEvent(loginChange);




        return  UserLogResultModel.ofOk(uid);
    }


    private long getLoginUserId(String userToken) {
        if (StringUtils.isEmpty(userToken)) {
            return INVALID_IM_USERID;
        }

        if (userToken.indexOf("%22") != -1) { // 解码
            userToken = URLDecoder.decode(userToken, StandardCharsets.UTF_8);
        }
        // ppu字符串的格式类似这样：userToken="UID=49529751616525&UN=rHcCBy&TT=885cfdaa0363283......"
        int pos = userToken.indexOf("userToken=\"");
        if (pos != -1) {
            userToken = userToken.substring(pos + 11);
        } else {
            // ppu字符串的格式类似这样：  "UID=50091816485396&UN=Z3UQCC&TT=dc9be2d......"
            pos = userToken.indexOf("\"UID=");
            if (pos != -1) {
                userToken = userToken.substring(pos + 1);
            }
        }
        String[] items = userToken.split("&");
        if (items.length == 0 || items.length == 1) {
            logger.error("act=get uid from userToken parse uid fail userToken={}", userToken);
            return INVALID_IM_USERID;
        }
        for (int i = 0; i < items.length; i++) {
            String[] oneItem = items[i].split("=");
            if (oneItem.length != 2)
                continue;
            if ("UID".equals(oneItem[0])) {
                return Long.parseLong(oneItem[1]);
            }
        }
        logger.error("get uid from userToken parse uid fail userToken={}", userToken);
        return INVALID_IM_USERID;
    }



    private void publishEvent(LoginChange loginChange) {

        Message<LoginChange> message =
                    Message.of(LOGIN_CHANGE_PUSH_TOPIC,loginChange);

        publishEvent(message);
        logger.info("publish Login   {} ",loginChange);
    }





}
