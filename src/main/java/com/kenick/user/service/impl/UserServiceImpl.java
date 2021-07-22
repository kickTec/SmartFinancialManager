package com.kenick.user.service.impl;

import com.kenick.generate.bean.User;
import com.kenick.generate.dao.UserMapper;
import com.kenick.user.service.UserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{

	//@Resource
	private UserMapper userDao;

	@Override
	public User getUserById(int userId) {
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
