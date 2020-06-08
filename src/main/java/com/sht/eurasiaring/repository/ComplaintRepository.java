package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Hongtao Shen
 * @date 2020/6/8 - 20:45
 **/
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    List<Complaint> findByUserId(Integer userId);
}
