package com.sht.eurasiaring.dao;

import com.sht.eurasiaring.entity.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 18:17
 **/
public interface CarouselDao extends JpaRepository<Carousel, Long>, JpaSpecificationExecutor<Carousel> {
}
