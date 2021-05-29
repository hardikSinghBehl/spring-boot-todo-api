package com.hardik.donatello.security.utility;

import java.util.List;

import org.springframework.security.core.userdetails.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {

	public User convert(com.hardik.donatello.entity.User user) {
		return new User(user.getEmail(), user.getPassword(), List.of());
	}

}