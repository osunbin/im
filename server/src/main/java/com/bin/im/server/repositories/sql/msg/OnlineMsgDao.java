package com.bin.im.server.repositories.sql.msg;

import com.bin.im.server.repositories.MsgGenerator;
import com.bin.im.server.repositories.model.DasResultModel;
import com.bin.im.server.repositories.model.cloud.CloudMsgIdModel;
import com.bin.im.server.repositories.model.cloud.QueryMsgModel;
import com.bin.im.server.repositories.sql.BaseDao;

import java.util.List;

public class OnlineMsgDao extends BaseDao {


    public DasResultModel<Long> saveOnlineMsg(OnlineMsg onlineMsg) {
        long msgId = MsgGenerator.generateMsgId();
        onlineMsg.setMsgId(msgId);
        getJdbi().useExtension(OnlineMsgSql.class,
                dao -> dao.saveOnlineMsg(onlineMsg));
        return DasResultModel.ofOk(msgId);
    }




    public DasResultModel<List<OnlineMsg>> queryOnlineMsg(long toUid,long fromUid,long lastMsgId,int count) {
        List<OnlineMsg> onlineMsgs = getJdbi().withExtension(OnlineMsgSql.class,
                dao -> dao.selectOnlineMsg(toUid, fromUid,lastMsgId,count));
        return DasResultModel.ofOk(onlineMsgs);
    }

    public DasResultModel<Integer> queryUnreadCount(QueryMsgModel queryMsg) {
        int unreadCount = getJdbi().withExtension(OnlineMsgSql.class,
                dao -> dao.selectUnreadCount(queryMsg));

        return DasResultModel.ofOk(unreadCount);
    }

    public void backwardMsg(OnlineMsg onlineMsg) {
        onlineMsg.setMsgFlag(2);
        getJdbi().useExtension(OnlineMsgSql.class,
                dao -> dao.backwardMsg(onlineMsg));
    }

    public void updateOnlineMsgByBackward(CloudMsgIdModel cloudMsgId) {

        getJdbi().useExtension(OnlineMsgSql.class,
                dao -> dao.updateOnlineMsgByBackward(cloudMsgId));
    }


    public DasResultModel<String> queryOnlineMsg(CloudMsgIdModel cloudMsgIdModel) {
        String msg = getJdbi().withExtension(OnlineMsgSql.class,
                dao -> dao.selectMsgContent(cloudMsgIdModel));
        return DasResultModel.ofOk(msg);
    }


}
