package com.sht.eurasiaring.service;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.entity.Praise;
import com.sht.eurasiaring.entity.Reply;
import com.sht.eurasiaring.repository.CommentRepository;
import com.sht.eurasiaring.repository.PraiseRepository;
import com.sht.eurasiaring.repository.ReplyRepository;
import com.sht.eurasiaring.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Hongtao Shen
 * @date 2020/5/24 - 19:59
 **/
@Service
public class PraiseService {

    @Autowired
    private PraiseRepository praiseRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;

    /**
     * @param type  "reply:600:600"  留言或者回复:留言的id:用户id
     * @param state "1"点赞 "0"取消点赞
     */
    public void save(Object type, Object state) {
        this.redisTemplate.boundHashOps("PraiseHash").put(type, state);
    }

    @Scheduled(cron = "*/8 * * * * ?")
    public void saveSql() {
        Set<Object> key = this.redisTemplate.boundHashOps("PraiseHash").keys();
        List<Object> value = this.redisTemplate.boundHashOps("PraiseHash").values();
        if (!key.isEmpty() && !value.isEmpty()) {
            int p = key.toString().length() - 1;
            String array0 = key.toString().substring(1, p);

            String[] array1 = array0.split(",");
            for (int i = 0; i < array1.length; i++) {
                this.redisTemplate.opsForHash().delete("PraiseHash", array1[i].trim());
            }
            for (int i = 0; i < value.size(); i++) {
                String[] num = array1[i].split(":");
                Praise praise = praiseRepository.findPraiseByTypeAndTypeIdAndUserId(num[0].trim(), Integer.valueOf(num[1].trim()), Integer.valueOf(num[2].trim()));
                if (value.get(i).equals("0")) {  //取消点赞
                    if (praise != null) {   //并且数据库中存在记录的
                        //this.praiseMapper.deleteByExample(example);
                        praiseRepository.deleteById(praise.getId());
                        //点赞数量-1
                        updateMessage(num, -1);
                    }
                } else if (praise != null) {  //点赞，并且数据库无记录的
                    Praise praiseInfo = new Praise();
                    praiseInfo.setType(num[0].trim());
                    praiseInfo.setTypeId(Integer.valueOf(num[1].trim()));
                    praiseInfo.setUserId(Integer.valueOf(num[2].trim()));
                    praiseInfo.setCreateTime(DateUtils.dateToString());
                    int id;
                    if ("comment".equals(num[0].trim())) {
                        Comment comment = commentRepository.findById(Integer.valueOf(num[1])).get();
                        id = comment.getUserId();
                        praise.setTypeUserId(id);
                    } else {
                        Reply reply = replyRepository.findById(Integer.valueOf(num[1])).get();
                        id = reply.getUserId();
                        praise.setTypeUserId(id);
                    }
                    praiseRepository.save(praise);
                    //给被点赞的新消息数+1
                    if (id != praise.getUserId()) {
                        redisTemplate.boundValueOps("eurasia_" + id).increment(1);
                    }
                    //点赞数量+1
                    updateMessage(num, 1);
                }
            }
        }
    }

    public void updateMessage(String[] type, Integer num) {
        if ("reply".equals(type[0].trim())) {
            Reply reply = replyRepository.findById(Integer.valueOf(type[1])).get();
            reply.setNumber(reply.getNumber() + num);
            replyRepository.save(reply);
        } else {
            Comment comment = commentRepository.findById(Integer.valueOf(type[1])).get();
            comment.setNumber(comment.getNumber() + num);
            commentRepository.save(comment);
        }
    }

}
