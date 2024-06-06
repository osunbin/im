package com.bin.im.server.repositories.sql.sm;

import com.bin.im.server.repositories.MsgGenerator;
import com.bin.im.server.repositories.model.DasResultModel;
import com.bin.im.server.repositories.sql.BaseDao;


public class SmOnlineMsgDao extends BaseDao {


    public DasResultModel<Long> saveOnlineMsg(SmOnlineMsg onlineMsg) {
        long msgId = MsgGenerator.generateMsgId();
        onlineMsg.setMsgId(msgId);
        getJdbi().useExtension(SmOnlineMsgSql.class,
                dao -> dao.saveOnlineMsg(onlineMsg));
        return DasResultModel.ofOk(msgId);
    }
}