package com.bin.im.entry.http.filter;

import com.bin.im.entry.http.NettyHttpRequest;
import com.bin.im.entry.http.NettyHttpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntiSpamFilter {

    private static final Logger logger = LoggerFactory.getLogger(AntiSpamFilter.class);

    private static volatile Set<String> deviceIdSet = new HashSet<>();
    private static volatile Set<String> ipSet = new HashSet<>();//从缓存内获取的封禁IP
    private static volatile Set<String> ipSet_1 = new HashSet<>();//从antispider服务获取的封禁IP
    private static volatile Set<String> uidSet = new HashSet<>();
    private static final long period = 60L * 1000;
    public static final String SPAM_KEY = "entry_";

    public void initCache() {
        /* 类加载时更新缓存 */
        deviceIdSet = updateCache("spam:deviceIds");
        ipSet = updateCache("spam:ips");
        uidSet = updateCache("spam:uids");
        /* 单线程后台定期更新缓存 */
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                deviceIdSet = updateCache("spam:deviceIds");
                ipSet = updateCache("spam:ips");
                uidSet = updateCache("spam:uids");
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }


    public NettyHttpResponse run(ChannelHandlerContext context, Object msg) {
        NettyHttpRequest httpReq = new NettyHttpRequest((FullHttpRequest) msg);


        String ip = getRemoteAddr(httpReq, context);

        String uid = httpReq.getHeader("uid");
        String deviceId = httpReq.getHeader("deviceId");


        FullHttpResponse resp = null;
        //反作弊拦截
        if (isContainsIp("filter2", ip) || isContainsDeviceId(deviceId) || isContainsUid(uid)) {

            StringBuilder json = new StringBuilder();
            json.append(httpReq.getHeader("callback")).append("(");
            json.append("{").append("\"respCode\":").append(-1).append(",\"errMsg\":").append("\"身份校验失败，请重试或重新登录\"").append("})");


            resp = NettyHttpResponse.ok(json.toString());
        }
        return (NettyHttpResponse) resp;
    }

    public static String getRemoteAddr(NettyHttpRequest httprequest, ChannelHandlerContext context) {
        String ip = httprequest.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httprequest.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httprequest.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostName();
        }

        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        return ip;
    }


    private Set<String> updateCache(String key) {
        String logStr = "anti spiderutil update " + key;
        String spamKey = SPAM_KEY + key;
        Set<String> container = new HashSet<String>();
        long startTime = System.currentTimeMillis();
        try {
            logger.info("{} begin key={}", logStr, spamKey);
            // TODO
           // container = jedis.smembers(spamKey);
            logger.info(logStr, " size=", container == null ? 0 : container.size());

            logger.info("{} finish, took:{}", logStr, (System.currentTimeMillis() - startTime));
        } catch (Exception ex) {
            logger.error("{} act=updateCache_error, ex=", logStr, exception2String(ex));
        } finally {

        }
        return container;
    }

    public static Boolean isContainsDeviceId(String DeviceId) {
        return !(DeviceId == null || DeviceId.trim().equals("")) && deviceIdSet.contains(DeviceId);
    }

    public static Boolean isContainsIp(String logStr, String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        if (ipSet.contains(ip)) {
            logger.info(logStr + " k=s act=entry_forbid_ip from=redis forbidIP=" + ip);
            return true;
        }
        if (ipSet_1.contains(ip)) {
            logger.info(logStr + " k=s act=entry_forbid_ip from=antispider forbidIP=" + ip);
            return true;
        }
        return false;
    }

    public static Boolean isContainsUid(String uid) {
        // return !(uid == null || uid.trim().equals("")) && (uidSet.contains(uid);
        if (uid == null || uid.trim().equals("")) {
            return false;
        }
        if (uidSet.contains(uid)) {
            return true;
        }
        return false;
    }


    public static String exception2String(Exception ex) {
        String exceptionMessage = "";
        if (ex != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            try {
                ex.printStackTrace(pw);
                exceptionMessage = sw.toString();
            } finally {
                try {
                    sw.close();
                    pw.close();
                } catch (Exception e) {
                }
            }
        }
        return exceptionMessage;
    }
}
