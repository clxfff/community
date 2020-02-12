package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtils;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author clx
 */
@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {

        if (user == null) {
            throw new IllegalArgumentException("注册信息为空");
        }

        Map<String, Object> map = new HashMap<>(16);

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        // 验证证号是否存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该用户名已存在！");
            return map;
        }

        // 验证邮箱是否存在
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtils.generateUUID().substring(0,5));
        user.setPassword(CommunityUtils.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtils.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);

        mailClient.sendMail(user.getEmail(), "激活账号", content);
        System.out.println(content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, String> login(String username, String password, int expiredSeconds) {

        Map<String, String> map = new HashMap<>(16);
        User user = userMapper.selectByName(username);
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码为空");
            return map;
        }

        if (user == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }

        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账户未激活");
            return map;
        }

        // 验证密码
        password = CommunityUtils.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtils.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }

    public Map<String, String> updatePassword(int userId, String oldPassword, String newPassword, String newPasswordAgain) {
        Map<String, String> map = new HashMap<>(16);

        if (StringUtils.isBlank(oldPassword)) {
            map.put("oldPasswordMsg", "原始密码为空！");
            return map;
        }

        if (StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "新密码为空！");
            return map;
        }

        if (StringUtils.isBlank(newPasswordAgain)) {
            map.put("newPasswordMsgAgain", "确认密码为空！");
            return map;
        }

        if (!newPassword.equals(newPasswordAgain)) {
            map.put("newPasswordMsg", "输入的两次密码不一致!");
            return map;
        }

        String password = CommunityUtils.md5(oldPassword + userMapper.selectById(userId).getSalt());
        if (!password.equals(userMapper.selectById(userId).getPassword())) {
            map.put("oldPasswordMsg", "原始密码不正确！");
            return map;
        }

        userMapper.updatePassword(userId, oldPassword);
        return map;
    }
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }
}
