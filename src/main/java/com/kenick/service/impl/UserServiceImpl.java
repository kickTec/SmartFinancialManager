package com.kenick.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kenick.dao.UserDao;
import com.kenick.entity.User;
import com.kenick.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	@Resource
	private UserDao userDao;

	@Override
	public User getUserById(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addUser(User user) {
		boolean result = false;
		try {
			userDao.insert(user);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
