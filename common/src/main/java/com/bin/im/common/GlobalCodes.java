package com.bin.im.common;

public class GlobalCodes {


    public static final int RESP_SUCCESS = 200000; //处理成功
    public static final int RESP_SERVER_ERROR = 500000; //服务器错误
    public static final int RESP_SERVER_DAS_CREATE_ANONYMSESSION = 500102; //创建匿名session失败
    public static final int RESP_SERVER_DAS_CHECK_ANONYMSESSION = 500103; //校验匿名session失败
    public static final int RESP_SERVER_CPC_ERROR = 500500;
    public static final int RESP_CLIENT_ERROR = 400000; //客户端错误
    public static final int RESP_CLIENT_OP_NOT_PERMIT = 400001; //操作不允许
    public static final int RESP_CLIENT_LACK_PARAM = 400002; //缺少参数
    public static final int RESP_CLIENT_NEED_VCODE = 400003; //要出验证码
    public static final int RESP_CLIENT_VCODE_WRONG = 400004; //验证码不正确
    public static final int RESP_CLIENT_REQ_FORBID = 400005; //禁止请求
    public static final int RESP_CLIENT_BLOCKED = 400006; //已封禁
    public static final int RESP_SMS_CODE = 400007; //出短信验证码
    public static final int RESP_CLIENT_NOT_LOGIN = 400009; //未登陆发送pick
    public static final int RESP_CLIENT_NEED_CAPTCHA = 400010; //客户端需要验证码
    public static final int RESP_CLIENT_NEED_FACE_AUTH = 400011; //客户端需要实名认证
    //密码错误
    public static final int USER_LOGIN_RESP_PASSWD_ERROR = 401011;
    //用户不存在
    public static final int RESP_CLIENT_ERROR_USERNOTEXIST = 401012;
    //cookie不合法
    public static final int USER_LOGIN_RESP_INVALID_COOKIE = 401013;
    public static final int USER_LOGIN_RESP_ILLEGAL_CLIENT_IP = 401014;//客户端ip非法
    public static final int USER_LOGIN_RESP_USERNAME_TOO_LONG = 401015;//用户名过长
    public static final int USER_LOGIN_RESP_CLIENT_VERSION_TOO_OLD = 401016;//客户端版本过久
    public static final int USER_LOGIN_RESP_COOKIE_ERROR = 401017;//cookie验证错误
    public static final int USER_LOGIN_RESP_SESSION_ERROR = 401018;//匿名session验证失败
    public static final int USER_KICK_OTHER_TERMINAL_RESP_NOT_LOGIN  = 401041; //用户没有登录


}
