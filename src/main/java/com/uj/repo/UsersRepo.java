package com.uj.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uj.entity.Users;

public interface UsersRepo extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByUserName(String userName);

}
