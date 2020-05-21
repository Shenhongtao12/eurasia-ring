package com.sht.eurasiaring.service;

import com.sht.eurasiaring.repository.MatterRepository;
import com.sht.eurasiaring.entity.Matter;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.JsonData;
import com.sht.eurasiaring.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Hongtao Shen
 * @date 2020/5/17 - 19:11
 **/
@Service
public class MatterService {

    @Autowired
    private MatterRepository matterRepository;

    @Autowired
    private CommentService commentService;

    public JsonData save(Matter matter) {
        if (StringUtils.isEmpty(matter.getUserId())){
            return JsonData.buildError("数据错误，未关联用户");
        }
        matter.setCreateTime(DateUtils.dateToString());
        matterRepository.save(matter);
        return JsonData.buildSuccess("成功");
    }

    //暂时不允许更新
    public JsonData update(Matter matter) {
        Matter result = matterRepository.findById(matter.getId()).get();
        if (StringUtils.isEmpty(result)){
            return JsonData.buildError("数据错误，不存在该matterId");
        }
        matterRepository.save(matter);
        return JsonData.buildSuccess("更新成功");
    }

    public JsonData delete(Integer id) {
        matterRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功");
    }

    public PageResult<Matter> init(){
        Page<Matter> page = matterRepository.findAll(PageRequest.of(0, 4));
        return new PageResult<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    public Matter findById(Integer matterId, Integer userId) {
        Matter matter = matterRepository.findById(matterId).get();
        matter.setCommentList(commentService.findByMatterId(matterId, userId));
        return matter;
    }

    public Integer findUserIdByMatterId(Integer matterId){
        return matterRepository.findUserIdById(matterId);
    }

    public Matter findMatterById(Integer matterId) {
        return matterRepository.findById(matterId).get();
    }
}
