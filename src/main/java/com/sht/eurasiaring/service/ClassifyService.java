package com.sht.eurasiaring.service;

import com.sht.eurasiaring.dao.ClassifyDao;
import com.sht.eurasiaring.entity.Classify;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 17:33
 **/
@Service
public class ClassifyService {

    @Autowired
    private ClassifyDao classifyDao;

    public Classify save(Classify classify) {
        return classifyDao.save(classify);
    }

    public List<Classify> findAll(){
        return classifyDao.findAll();
    }

    public JsonData update(Classify classify) {
        Optional<Classify> classifyOptional = classifyDao.findById(classify.getId());
        if (!classifyOptional.isPresent()){
            return JsonData.buildError("数据错误");
        }
        classifyDao.save(classify);
        return JsonData.buildSuccess("成功");
    }
}
