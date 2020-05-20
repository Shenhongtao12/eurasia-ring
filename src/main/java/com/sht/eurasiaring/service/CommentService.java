package com.sht.eurasiaring.service;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.repository.CommentRepository;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
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

    //添加留言
    public JsonData save(Comment comment) {
        return JsonData.buildSuccess(commentRepository.save(comment),"成功");
    }

    //删除留言和留言的回复
    public JsonData delete(Long id) {
        int num = replyService.deleteByCommentId(id);
        commentRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功,并删除"+ num + "条回复内容");
    }

    //根据MatterId查询留言回复
    public List<Comment> findByMatterId(Long matterId, Long userId){
        List<Comment> commentList = commentRepository.findByMatterId(matterId);
        for (Comment comment : commentList) {
            comment.setReplyList(replyService.getTreeReply(comment.getCommentId(), userId));
        }
        return commentList;
    }
}
