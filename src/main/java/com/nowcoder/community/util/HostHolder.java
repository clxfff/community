package com.nowcoder.community.util;


import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author clx
 * 持有用户信息，用于代替session
 */
@Component
public class HostHolder {

    ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
