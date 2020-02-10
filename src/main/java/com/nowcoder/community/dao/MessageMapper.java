package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author clx
 */
@Mapper
@Component
public interface MessageMapper {

    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * @param userId 用户Id
     * @param offset 起始页
     * @param limit 每页最多的条数
     * @return 装有多条会话的列表
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     * @param userId 用户Id
     * @return 用户私信的数量
     */
    int selectConversationCount(int userId);

    /**
     * 查询某个会话包含的私信列表
     * @param conversationId 会话Id
     * @param offset 起始页
     * @param limit 每页最多显示条数
     * @return
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId 会话Id
     * @return 私信数量
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     * @param userId 用户Id
     * @param conversationId 会话Id
     * @return 未读私信数量
     */
    int selectLetterUnreadCount(int userId, String conversationId);

}
