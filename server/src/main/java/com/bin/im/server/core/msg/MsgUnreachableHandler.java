package com.bin.im.server.core.msg;


import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.cache.MsgTimeoutManager;
import com.bin.im.server.core.cache.TimeWheelManager;
import com.bin.im.server.domain.MsgAckInfo;
import com.bin.im.server.domain.UnreachableMsg;
import com.bin.im.server.spi.annotation.Service;
import org.apache.dubbo.config.annotation.DubboService;


/**
 * 实现消息未送达业务 （entry发送unreachable到logic，
 * logic清理缓存，从时间轮中移除记录，调用MsgDiscard接口处理假在线情况）
 */
@DubboService
@Service
public class MsgUnreachableHandler extends BaseHandler {




    private void unreachable(UnreachableMsg unreachableMsg) {
        logger.info("unreachable msg:{}", unreachableMsg);
        long toUid = unreachableMsg.getToUid();
        int sourceType = unreachableMsg.getSourceType();
        getUserInfoRouter().delUserLoginInfo(toUid, sourceType);
        logger.info("clear user info cache uid:{}  sourceType:{}", toUid, sourceType);


        MsgAckInfo pAckInfo = new MsgAckInfo();
        pAckInfo.setFromUid(unreachableMsg.getFromUid());
        pAckInfo.setToUid(toUid);
        pAckInfo.setMsgId(unreachableMsg.getMsgId());
        pAckInfo.setSourceType(sourceType);
        pAckInfo.setMsgContent(unreachableMsg.getMsgContent());
        pAckInfo.setMsgType(unreachableMsg.getMsgType());
        pAckInfo.setTimeout(1);

        String ackKey = pAckInfo.getAckKey();

        TimeWheelManager timeWheel =
                  getService(TimeWheelManager.SERVICE_NAME);
        timeWheel.dealAck(ackKey);

        logger.info("clear ack to timewheel  ackKey:{} ",  ackKey);

        MsgTimeoutManager msgTimeoutManager =
                getService(MsgTimeoutManager.SERVICE_NAME);

        msgTimeoutManager.discardMsg(pAckInfo);
        logger.info("deal false online msg:{}", pAckInfo);
    }


}
