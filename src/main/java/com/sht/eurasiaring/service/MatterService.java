package com.sht.eurasiaring.service;

import com.sht.eurasiaring.entity.Matter;
import com.sht.eurasiaring.repository.MatterRepository;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hongtao Shen
 * @date 2020/5/23 - 13:33
 **/
@Service
public class MatterService {
    @Autowired
    private MatterRepository matterRepository;
    @Autowired
    private PostService postService;

    public List<Matter> init(){
        List<Matter> matterList = new ArrayList<>();
        Matter matter = matterRepository.findById(0).get();
        String[] ids = matter.getMatterUrl().split(",");
        for (String id : ids) {
            Matter matter1 = matterRepository.findById(Integer.valueOf(id)).get();
            matter1.setMatterNum(postService.countNumByMatter(matter1.getId()));
            matterList.add(matter1);
        }
        return matterList;
    }

    public JsonData save(Matter matter) {
        matter.setCreateTime(DateUtils.dateToString());
        Matter save = matterRepository.save(matter);
        return JsonData.buildSuccess(save, "成功");
    }

    public JsonData hotMatter(String hot) {
        String[] hots = hot.split(",");
        if (hots.length > 4){
            return JsonData.buildError("数据错误");
        }
        Matter matter = matterRepository.findById(0).get();
        matter.setMatterUrl(hot);
        matterRepository.save(matter);
        return JsonData.buildSuccess("更改热门话题成功");
    }
}
