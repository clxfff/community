package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author clx
 */
public class CommunityUtils {

    /**
     * 生成只带有字母和数字的随机字符串
     * @return 随机字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
