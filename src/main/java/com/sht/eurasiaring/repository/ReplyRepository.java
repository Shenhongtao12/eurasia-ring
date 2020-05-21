package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Reply;
import com.sht.eurasiaring.utils.MessageUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByCommentId(Integer id);

    Integer deleteByCommentId(Integer id);

    List<Reply> findByUserId(Integer userId);

}
