package com.kenick.service;

import com.kenick.entity.User;

public interface UserService {
	public User getUserById(int userId);
	
	boolean addUser(User user);
}
