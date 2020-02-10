package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author clx
 */
@Mapper
@Component
public interface CommentMapper {

    /**
     * 通过实体来查询评论
     * @param entityType 实体类型
     * @param entityId 实体的Id
     * @param offset 起始行
     * @param limit 一页的显示限制
     * @return 评论的集合
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 通过实体来查询评论总数
     * @param entityType 实体类型
     * @param entityId 实体Id
     * @return 查询结果的数量
     */
    int selectCountByEntity(int entityType, int entityId);

    /**
     * 添加评论
     * @param comment 评论
     * @return 修改的行数
     */
    int insertComment(Comment comment);
}
