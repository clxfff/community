package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @author clx
 */
@Mapper
@Component
public interface LoginTicketMapper {

    /**
     * 向数据库插入一条登录凭证
     * @param loginTicket 登录凭证
     * @return 改变的行数
     */
    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 通过凭证查询出值
     * @param ticket 凭证值
     * @return 登录凭证
     */
    @Select({
            "select id, user_id, ticket, status, expired ",
            "from login_ticket where ticket = #{ticket};"
    })
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新一个用户的登录凭证的状态
     * @param ticket 凭证值
     * @param status 状态
     * @return 改变的行数
     */
    @Update({
            "update login_ticket set status = #{status} where ticket = #{ticket}"
    })
    int updateStatus(String ticket, int status);
}
