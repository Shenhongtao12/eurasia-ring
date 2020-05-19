package com.sht.eurasiaring.service;

import com.sht.eurasiaring.dao.MatterDao;
import com.sht.eurasiaring.entity.Matter;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.JsonData;
import com.sht.eurasiaring.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Hongtao Shen
 * @date 2020/5/17 - 19:11
 **/
@Service
public class MatterService {

    @Autowired
    private MatterDao matterDao;

    public JsonData save(Matter matter) {
        if (StringUtils.isEmpty(matter.getUserId())){
            return JsonData.buildError("数据错误，未关联用户");
        }
        matter.setCreateTime(DateUtils.dateToString());
        matterDao.save(matter);
        return JsonData.buildSuccess("成功");
    }

    //暂时不允许更新
    public JsonData update(Matter matter) {
        Matter result = matterDao.findById(matter.getId()).get();
        if (StringUtils.isEmpty(result)){
            return JsonData.buildError("数据错误，不存在该matterId");
        }
        matterDao.save(matter);
        return JsonData.buildSuccess("更新成功");
    }

    public JsonData delete(Long id) {
        matterDao.deleteById(id);
        return JsonData.buildSuccess("删除成功");
    }

    public PageResult<Matter> init(){
        Page<Matter> page = matterDao.findAll(PageRequest.of(0, 4));
        return new PageResult<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    public Matter findById(Long matterId) {
        Matter matter = matterDao.findById(matterId).get();
        System.out.println("11111111111"+matter);
        //matter.setCommentList();
        return matter;
    }
}
