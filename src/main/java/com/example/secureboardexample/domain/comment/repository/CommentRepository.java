package com.example.secureboardexample.domain.comment.repository;

import com.example.secureboardexample.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}
