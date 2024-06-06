package com.bin.im.server.repositories.sql.sys;

import com.bin.im.server.repositories.MsgGenerator;
import com.bin.im.server.repositories.model.DasResultModel;
import com.bin.im.server.repositories.model.QueryBaseMsgModel;
import com.bin.im.server.repositories.sql.BaseDao;


import java.util.List;


public class SystemMsgDao extends BaseDao {





    public DasResultModel<List<SystemMsg>> querySysMsg(QueryBaseMsgModel queryMsgModel) {
        List<SystemMsg> systemMsgs = getJdbi().withExtension(SystemMsgSql.class, dao -> dao.selectSysMsg(queryMsgModel));
        return DasResultModel.ofOk(systemMsgs);
    }


    public DasResultModel<Long> saveSysMsg(SystemMsg systemMsg) {
        long msgId = MsgGenerator.generateSysMsgId();
        systemMsg.setMsgId(msgId);
        getJdbi().useExtension(SystemMsgSql.class,dao -> dao.saveSysMsg(systemMsg));
        return DasResultModel.ofOk(msgId);
    }


    public DasResultModel<List<SystemBroadCastMsg>> querySystemBroadCastMsg(long uid,long lastMsgId,int count) {
        List<SystemBroadCastMsg> systemBroadCastMsgs = getJdbi().withExtension(SystemBroadCastMsgSql.class, dao -> dao.selectSysBroadCastMsg(uid, lastMsgId,count));
        return DasResultModel.ofOk(systemBroadCastMsgs);
    }

    public DasResultModel<List<SystemMsgContent>> querySysMsgContents(List<Long> msgIdList) {
        List<SystemMsgContent> systemMsgContents = getJdbi().withExtension(SystemMsgContentSql.class, dao -> dao.selectSysMsgContents(msgIdList));
        return DasResultModel.ofOk(systemMsgContents);
    }

    public DasResultModel<SystemMsgContent> querySysMsgContent(long msgId) {
        SystemMsgContent systemMsgContent = getJdbi().withExtension(SystemMsgContentSql.class, dao -> dao.selectSysMsgContent(msgId));
        return DasResultModel.ofOk(systemMsgContent);
    }


}
