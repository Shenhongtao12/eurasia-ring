package com.sht.eurasiaring.service;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.repository.CommentRepository;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private MatterService matterService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    //添加留言
    public JsonData save(Comment comment) {
        Comment save = commentRepository.save(comment);
        int matterUserId = matterService.findUserIdByMatterId(comment.getMatterId());
        if (comment.getUserId() != matterUserId) {
            this.redisTemplate.boundValueOps("eurasia_" + matterUserId).increment(1);
        }
        return JsonData.buildSuccess(save,"成功");
    }

    //删除留言和留言的回复
    public JsonData delete(Integer id) {
        int num = replyService.deleteByCommentId(id);
        commentRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功,并删除"+ num + "条回复内容");
    }

    //根据MatterId查询留言回复
    public List<Comment> findByMatterId(Integer matterId, Integer userId){
        List<Comment> commentList = commentRepository.findByMatterId(matterId);
        for (Comment comment : commentList) {
            comment.setReplyList(replyService.getTreeReply(comment.getCommentId(), userId));
        }
        return commentList;
    }
}
