package com.example.secureboardexample.domain.post.repository;

import com.example.secureboardexample.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
