package com.example.restfulwebservices.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restfulwebservices.user.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
