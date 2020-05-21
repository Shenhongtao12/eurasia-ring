package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMatterId(Long matterId);
}