CREATE TABLE `t_user_contact`
(
    `uidA`         bigint NOT NULL  COMMENT '当前用户',
    `uidB`         bigint NOT NULL   COMMENT '好友',
    `direction` tinyint NOT NULL COMMENT '1 - a add b 0 - b add a',
    `unread_count`  int     NOT NULL DEFAULT 0 COMMENT '未读消息数量',
    `last_ack_msgid`  bigint NOT NULL COMMENT '最后一次读取的消息id',
    `read_time`     bigint   NOT NULL DEFAULT 0 COMMENT '最后一次读取的消息的时间',
    `last_time`     bigint   NOT NULL DEFAULT 0 COMMENT '最后一次两人联系的时间',
    `contacts_type` tinyint NOT NULL 0 COMMENT '普通 商家 系统',
    `add_time`  bigint  NOT NULL,
    `contacts_flag`        tinyint  NOT NULL DEFAULT 0 COMMENT '删除 ',
    PRIMARY KEY (`uidA`,`uidB`) USING BTREE
)ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT = 'im 用户联系人表';
--   AUTO_INCREMENT = 1

CREATE TABLE `t_user_online_msg`
(
    `msg_id`          bigint  NOT NULL ,
    `to_uid`         bigint      DEFAULT NULL,
    `from_uid`         bigint DEFAULT NULL,
    `client_msg_id`        bigint   DEFAULT NULL COMMENT '客户端唯一id',
    `direction`        tinyint DEFAULT NULL COMMENT '正反向',
    `msg_type`        int      DEFAULT NULL,
    `msg_Content`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '事务编号',
    `timestamp`    bigint  NOT NULL,
    `msg_flag`  tinyint DEFAULT NULL,
    PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_user_online_msg`
(
    `uid`         bigint NOT NULL,
    `contacts_time`    bigint  NOT NULL COMMENT '用户好友列表更新时间戳',
    PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_user_group`
(
    `uid`         bigint NOT NULL,
    `gid`         bigint NOT NULL,
    `join_msgId`    bigint  NOT NULL,
    `read_time`    bigint  NOT NULL,
    `last_ack_msgId`    bigint  NOT NULL,
    `join_time`    bigint  NOT NULL,
    `join_type`    bigint  NOT NULL,
    `approval_uid`    bigint  NOT NULL,
    `quit_time`    bigint  NOT NULL,
    PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `t_group_msg`
(
    `msg_id`          bigint  NOT NULL,
    `gid`         bigint NOT NULL,
    `suid`         bigint NOT NULL,
    `client_msg_id`        bigint   DEFAULT NULL COMMENT '客户端唯一id',
    `msg_type`        int      DEFAULT NULL,
    `msg_Content`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
    `send_time`    bigint  NOT NULL,
    `msg_flag`  tinyint DEFAULT NULL,
    PRIMARY KEY (`gid`,`msg_id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_group_msg_ack`
(
    `id`          bigint  NOT NULL,
    `gid`         bigint NOT NULL,
    `suid`         bigint NOT NULL,
    `recv_uid`         bigint NOT NULL,
    `msg_id`          bigint  NOT NULL,
    `is_ack`  tinyint DEFAULT NULL,
    `read_time`    bigint  NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_group_times`
(
    `gid`         bigint NOT NULL,
    `ginfo_time`    bigint  NOT NULL  COMMENT '群源信息更新时间 ',
    `gulist_time`    bigint  NOT NULL COMMENT '群成员列表更新时间 ',
    `gmsg_time`    bigint  NOT NULL COMMENT '群消息最后一次发送时间',
    PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT = 'im 登录时拉取群数据';


CREATE TABLE `t_user_sys_msg`
(
    `msg_id`          bigint  NOT NULL,
    `to_uid`         bigint      DEFAULT NULL,
    `from_uid`         bigint DEFAULT NULL,
    `msg_type`        int      DEFAULT NULL,
    `timestamp`    bigint  NOT NULL,
    `msg_content`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;