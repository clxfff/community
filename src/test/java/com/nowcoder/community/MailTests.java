package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
    @Autowired
    MailClient mailClient;

    @Autowired
    TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("751668825@qq.com", "Test", "Welcome.");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "萧桑榆");
        String content = templateEngine.process("/mail/demo", context);

        mailClient.sendMail("clxfff@foxmail.com", "Html", content);
        System.out.println(content);
    }
}
