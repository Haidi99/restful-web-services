package com.example.restfulwebservices.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restfulwebservices.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
