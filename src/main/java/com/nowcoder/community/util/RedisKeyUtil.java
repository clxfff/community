package com.nowcoder.community.util;

/**
 * @author clx
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    /**
     * 某个实体的赞
     * like:entity:entityType:entityId set -> {userId}
     * @param entityType 实体类型：帖子、评论
     * @param entityId 实体ID
     * @return 实体点赞的key
     */
    public static String getEntityLikeKey(int entityType, int entityId) {

        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
