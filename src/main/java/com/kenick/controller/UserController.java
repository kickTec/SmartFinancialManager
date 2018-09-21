package com.kenick.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kenick.entity.User;
import com.kenick.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
    @RequestMapping("/addUser")
    @ResponseBody
    public User addUser(HttpServletRequest request, Model model){
        String userId = request.getParameter("id").toString();
        User user = new User();
        user.setId(userId);
        user.setUsername(userId+"test");
        this.userService.addUser(user);
        return user;
    }
}