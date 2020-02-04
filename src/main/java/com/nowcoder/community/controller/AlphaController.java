package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @author clx
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello () {
        return "Hello SpringBoot";
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        System.out.println(request.getParameter("code"));
        response.setContentType("test/html, charset=utf8");
        try (
            PrintWriter writer = response.getWriter();
        ){
           writer.write("<h1>hello</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("code", CommunityUtils.generateUUID());
        cookie.setPath("/community/alpha");
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return "set cookie";
    }
}
