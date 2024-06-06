package com.bin.im.server.core;

import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.model.OfflineMsgData;
import com.bin.im.server.repositories.ImDas;
import com.bin.im.server.spi.ImEngine;
import com.bin.im.server.spi.impl.ServerContext;
import com.bin.im.server.spi.impl.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bin.im.server.event.EventService.OFFLINE_PUSH_TOPIC;

/**
 *  msgid 生成 前端携带 然后对比,小就下一秒
 */
public class BaseHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected int onlineMsgPageSize = 10;

    protected ImEngine imEngine;

    public void init(ImEngine imEngine) {
        if (this.imEngine == null) {
            this.imEngine = imEngine;
            init();
        }

    }


    public void init(){}

    public void publishOfflineEvent(Message<?> message) {
        message.setTopic(OFFLINE_PUSH_TOPIC);
        imEngine.getEventService().publishEvent(message);
    }

    public void publishEvent(Message<?> message) {
        imEngine.getEventService().publishEvent(message);
    }


    public <T> T getService(String serviceName) {
      return  imEngine.getService(serviceName);
    }

    public void repairSendUserInfo(long uid,LoginInfo fromLogin) {
        if (fromLogin != null) return;
        ServerContext currContext = ServiceManager.getCurrContext();
        if (currContext == null) {
            throw new NullPointerException("uid="+uid+" serverContext is null ");
        }
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUid(uid);
        loginInfo.setLoginESIp(currContext.getEsIp());
        loginInfo.setLoginESPort(currContext.getEsPort());
        loginInfo.setCliType(currContext.getSourceType());

        getUserInfoRouter().addUserLoginInfo(uid,loginInfo);
    }

    public UserInfoRouter getUserInfoRouter() {
       return imEngine.getUserInfoRouter();
    }

    public BaseHandler(ImEngine imEngine) {

           this.imEngine = imEngine;
    }

    public BaseHandler() {

    }


    public ImDas imDas() {
        return imEngine.getImDas();
    }

    public ImEngine getImEngine() {
        return imEngine;
    }


    public String getServiceName() {
        return "BaseHandler";
    }
}
