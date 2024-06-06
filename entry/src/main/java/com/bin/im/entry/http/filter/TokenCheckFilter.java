package com.bin.im.entry.http.filter;

import com.bin.im.common.internal.utils.StringUtils;
import com.bin.im.entry.http.NettyHttpRequest;
import com.bin.im.entry.http.NettyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;

public class TokenCheckFilter {

    public static Logger logger = LoggerFactory.getLogger(TokenCheckFilter.class);




    public NettyHttpResponse run(ChannelHandlerContext context, Object msg) {
        NettyHttpRequest httpReq = new NettyHttpRequest((FullHttpRequest)msg);
        NettyHttpResponse httpResp = (NettyHttpResponse)NettyHttpResponse.make(UNAUTHORIZED);


        // 获取Seesion
        Set<Map.Entry<String, String>> cookies = httpReq.getCookies();
        String ppuCookie = null;
        Long uidCookie = null;
        if (cookies != null && cookies.size() > 0) {
            for (Map.Entry<String, String> cookie : cookies) {
                if ("ppu".equalsIgnoreCase(cookie.getKey())) {
                    ppuCookie = cookie.getValue();
                }
                if ("uid".equalsIgnoreCase(cookie.getKey())) {
                    uidCookie = Long.valueOf(cookie.getValue());
                }
            }
        }

        Long realUid = checkPpu(ppuCookie, uidCookie);
        if (realUid == null) {
            /**
             * 校验失败
             */
            return httpResp;
        }

        return null;
    }



    private Long checkPpu(String jwt, Long uid) {
        if (StringUtils.isEmpty(jwt) || uid == null) {
            logger.info(" tokenCheckFilter  jwt 是空的，校验不通过 ");
            return null;
        }
        return uid;
    }



    public String create(String inStr) {

        String valueTokenMd5Str = "apolloConfigItems.getAuthTokenMd5Str()";

        if(!"".equals(inStr)){
            String fStr = inStr.substring(0, 1);
            String lStr = inStr.substring(1, inStr.length());
            return string2Md5(fStr + valueTokenMd5Str + lStr);
        }
        return null;
    }

    public boolean checkToken(Long userId) {
        String token = "2312";
        if (!"".equals(token)) {
            String authenticatedToken = create(userId + "");
            if (authenticatedToken != null && authenticatedToken.equals(token)) {
                return true;
            }
        }
        return false;
    }


    public String string2Md5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}

