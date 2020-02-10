package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author clx
 */
@SuppressWarnings("ALL")
@Mapper
@Component
public interface DiscussPostMapper {

    /**
     * 当在个人主页查询“我的帖子”的时候使用
     * @param userId 用户id
     * @param offset 分页开始的号码
     * @param limit 一页的最大帖子数
     * @return “帖子”的数据集
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 注解用于给参数取别名
     * 如果只有一个参数，并且在<if>里使用，则必须加别名
     * @param userId 用户Id
     * @return 查询帖子的数量
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 发布帖子
     * @param discussPost 帖子
     * @return 改变的行数
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据主键查询帖子
     * @param id 主键
     * @return 查询到的帖子
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新评论的数量
     * @param id 帖子Id
     * @param commentCount 评论数量
     * @return 改变的行数
     */
    int updateCommentCount(int id, int commentCount);

}
