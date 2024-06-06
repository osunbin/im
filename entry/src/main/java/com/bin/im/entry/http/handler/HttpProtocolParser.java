package com.bin.im.entry.http.handler;

import com.bin.im.common.internal.utils.IPConverter;
import com.bin.im.common.id.self.IdGenerate;
import com.bin.im.common.internal.wheel.HashedWheelTimer;
import com.bin.im.entry.common.NacosHelper;
import com.bin.im.entry.config.EntryConfig;
import com.bin.im.entry.http.NettyHttpRequest;
import com.bin.im.entry.spi.EntryInvoker;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;


import static com.bin.im.common.ImAttachment.CLIENT_IP;
import static com.bin.im.common.ImAttachment.ID;
import static com.bin.im.common.internal.utils.MDCUtil.LOG_ID;
import static com.bin.im.common.internal.utils.MDCUtil.LOG_PRE;
import static com.bin.im.entry.cache.RequestSessionCacheManager.sessionCache;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 *  curl "http://127.0.0.1:8081/api/user/getUserInfo?key=1" -d "k=2" -H "Cookie: x=y; uid=1; ppu=1"
 *  xxx.com/im/{module}/{cmd}?xxx
 *  @DubboService(interfaceClass = IUserService.class, group = "user")
 *
 *   语法：{module}_{cmd}={apiInterface}:{method}
 *   示例：user_getUserInfo=com.nx.platform.user.remote.IUserService:getUserInfo
 */
public class HttpProtocolParser implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(HttpProtocolParser.class);

    private EntryConfig entryConfig;

    private EntryInvoker entryInvoker;

    private NettyHttpRequest httpRequest;

    private Channel channel;





    public HttpProtocolParser(NettyHttpRequest nettyHttpRequest, Channel channel) {
        this.httpRequest = nettyHttpRequest;
        this.channel = channel;
    }

    @Override
    public void run() {


        /**
         *  校验 存储 请求
         */
        String module = httpRequest.getStringPathValue(2);
        String cmd = httpRequest.getStringPathValue(3);

        Map<String, Object> parameterMap = httpRequest.getParameterMap();

        List<String> httpHeaders = entryConfig.getHttpHeaders();
        for (String httpHeader : httpHeaders) {
            String header = httpRequest.getHeader(httpHeader);
            parameterMap.put(httpHeader,header);
        }

        String clientIp = httpRequest.getHeader(CLIENT_IP);
        ReferenceCountUtil.release(httpRequest);

        Object uidObj = parameterMap.get("uid");
        if (Objects.nonNull(uidObj)) {
            return;
        }

        long logId = IdGenerate.genLogId(Long.parseLong(uidObj.toString()));

        String logPre = " logId: " + logId  +"  clientIp: " + clientIp + " module: " + module + " cmd " + cmd;



        String confStr = NacosHelper.getCmdConfig(module, cmd);
        // 判断请求是否注册
        if (null == confStr || confStr.isEmpty()) {
            logger.error(logPre + "no_command_found");
            return; // 接口找不到服务
        }

        // 解析配置--com.xxx.IUserService:getUserInfo
        String[] configList = confStr.split(":");
        String apiInterface = configList[0];
        String method = configList[1];

        logger.info("logId:{} {} {} im http entry ",module,cmd,logId);

        Object reqUidObj = parameterMap.get("reqUid");
        if (reqUidObj == null){
            return;
        }



        Map<String, Object> attachments =
                entryConfig.buildEntryAttachments();
        attachments.put(LOG_ID,logId);
        attachments.put(CLIENT_IP, IPConverter.ip2Int(clientIp));
        attachments.put(LOG_PRE,logPre);


        // TODO
        if (isSendIdLoadBalance(module, cmd)) {
            String reqUid = reqUidObj.toString();

            if (isGroup(module, cmd)) {
                Object groupIdObj = parameterMap.get("groupId");
                if (groupIdObj == null)
                    return;

                reqUid = groupIdObj.toString();
            }
            attachments.put(ID,reqUid);
        }

        // TODO
        long session = sessionCache.createSession();

        sessionCache.addChannel(session,channel);

      //  entryInvoker.invoker(module,apiInterface,method,parameterMap, attachments);


        /**
         *  tcp
         *  TODO
         * session.max.timeout.normal : 1500    25 分钟
         *
         *   interval :  5
         *
         *   http session 21
         */
        new HashedWheelTimer(5,SECONDS,300);

    }





    private boolean isSendIdLoadBalance(String module,String cmd) {
        return false;
    }


    private boolean isGroup(String module,String cmd) {
        return false;
    }
}

