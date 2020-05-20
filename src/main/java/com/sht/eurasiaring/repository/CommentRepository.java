package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
