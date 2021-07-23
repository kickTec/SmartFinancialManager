package com.kenick.user.service.impl;

import com.kenick.user.bean.User;
import com.kenick.user.service.UserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Override
	public User getUserById(int userId) {
		return null;
	}

	@Override
	public boolean addUser(User user) {
		return false;
	}

}
