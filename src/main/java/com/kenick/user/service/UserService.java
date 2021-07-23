package com.kenick.user.service;


import com.kenick.user.bean.User;

public interface UserService {
	public User getUserById(int userId);
	
	boolean addUser(User user);
}
