package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
    List<Comment> findByPostId(Integer postId);

}
