package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hongtao Shen
 * @date 2020/5/17 - 19:11
 **/
public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findUserIdById(Integer postId);


    Integer countByMatterId(Integer matterId);
}
