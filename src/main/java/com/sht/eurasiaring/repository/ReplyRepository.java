package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long id);

    Integer deleteByCommentId(Long id);

}
