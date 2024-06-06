package com.bin.im.common.encrypt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

public class TokenUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String SECRET_KEY = "secret_key";

    // 生成 Token
    public static String generateToken(String payload) {
        try {
            // 创建 HMAC-SHA256 密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);

            // 计算签名
            byte[] signatureBytes = mac.doFinal(payload.getBytes());

            // 使用 Base64 编码签名
            String signature = Base64.getEncoder().encodeToString(signatureBytes);

            // 返回 Token，格式为 payload.signature
            return payload + "." + signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 校验 Token
    public static boolean verifyToken(String token) {
        try {
            // 提取载荷和签名
            String[] parts = token.split("\\.");
            String payload = parts[0];
            String receivedSignature = parts[1];

            // 创建 HMAC-SHA256 密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);

            // 计算签名
            byte[] calculatedSignatureBytes = mac.doFinal(payload.getBytes());
            String calculatedSignature = Base64.getEncoder().encodeToString(calculatedSignatureBytes);

            // 验证签名是否一致
            if(!receivedSignature.equals(calculatedSignature)){
            	return false;
			}
           // 提取过期时间
            String expirationTime = new String(Base64.getDecoder().decode(payload)).split("\"expires_at\":\"")[1].split("\"")[0];

            // 检查有效期
            LocalDateTime expiresAt = LocalDateTime.parse(expirationTime);
            LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
            return currentTime.isBefore(expiresAt);
            
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 示例
    public static void main(String[] args) {
        // 生成载荷
        String payload = "{\"user_id\": \"123456\", \"username\": \"example_user\", \"expires_at\": \"2024-03-31T00:00:00Z\"}";

        // 生成 Token
        String token = generateToken(payload);
        System.out.println("Generated Token: " + token);

        // 校验 Token
        boolean isValid = verifyToken(token);
        System.out.println("Token is valid: " + isValid);
    }
}
