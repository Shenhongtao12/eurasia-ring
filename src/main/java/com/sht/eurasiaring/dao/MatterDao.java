package com.sht.eurasiaring.dao;

import com.sht.eurasiaring.entity.Matter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hongtao Shen
 * @date 2020/5/17 - 19:11
 **/
public interface MatterDao extends JpaRepository<Matter, Long> {
}
