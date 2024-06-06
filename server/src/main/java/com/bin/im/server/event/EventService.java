package com.bin.im.server.event;

/**
 *
 */
public interface EventService {

    /**
     * 用户登录状态变更通知
     */
    String LOGIN_CHANGE_PUSH_TOPIC = "loginChange_TOPIC";
    String LOGIN_CHANGE_PUSH_GROUP = "loginChange_GROUP";

    /**
     * 离线PUSH通知
     */
    String OFFLINE_PUSH_TOPIC = "offlinePush_TOPIC";
    String OFFLINE_PUSH_GROUP = "offlinePush_GROUP";

    /**
     * 系统消息
     */
    String SYS_MSG_TOPIC = "sysMsg_TOPIC";
    String SYS_MSG_GROUP = "sysMsg_GROUP";

    /**
     * keep alive
     */
    String KEEP_ALIVE_TOPIC = "keepAlive_TOPIC";
    String KEEP_ALIVE_GROUP = "keepAlive_GROUP";


    /**
     * 发消息
     */
    String ONLINE_MSG_TOPIC = "onlineMsg_TOPIC";
    String ONLINE_MSG_GROUP = "onlineMsg_GROUP";
    String ONLINE_MSG_TAG = "kfim";



    //  个人系统消息
    String GROUP_PERSONAL_SYS_MSG = "group_personal_sys_msg";
    String TOPIC_PERSONAL_SYS_MSG = "topic_personal_sys_msg";

    // 广播系统消息
    String GROUP_BROADCAST_SYS_MSG = "group_broadcast_sys_msg";
    String TOPIC_BROADCAST_SYS_MSG = "topic_broadcast_sys_msg";



    void registerListener(String topic, MessageListener<?> listener);



    void publishEvent(Message<?> message);


}
