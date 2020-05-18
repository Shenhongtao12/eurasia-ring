package com.sht.eurasiaring.service;

import com.sht.eurasiaring.dao.CarouselDao;
import com.sht.eurasiaring.entity.Carousel;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 18:17
 **/
@Service
public class CarouselService {
    @Autowired
    private CarouselDao carouselDao;

    public void save(Carousel carousel) {
        carouselDao.save(carousel);
    }

    public JsonData update(Carousel carousel) {
        Optional<Carousel> carouselOptional = carouselDao.findById(carousel.getId());
        if (!carouselOptional.isPresent()){
            return JsonData.buildError("数据错误");
        }
        carouselDao.save(carousel);
        return JsonData.buildSuccess("更新成功");
    }

    public List<Carousel> findAll(){
        return carouselDao.findAll();
    }
}
