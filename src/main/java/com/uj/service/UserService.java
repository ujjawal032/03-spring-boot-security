package com.uj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.uj.entity.Users;
import com.uj.repo.UsersRepo;

public class UserService implements UserDetailsService {
	
	@Autowired
	private UsersRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> findByUserName = repo.findByUserName(username);
		
		return findByUserName.map(UserInfoService::new)
		                                 .orElseThrow(()-> new UsernameNotFoundException("user not found ::"+username));
	}

}
