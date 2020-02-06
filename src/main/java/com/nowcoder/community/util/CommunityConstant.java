package com.nowcoder.community.util;

/**
 * @author clx
 */
public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 登录凭证默认有效时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 10;


    /**
     * 记住密码后登录凭证有效时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
