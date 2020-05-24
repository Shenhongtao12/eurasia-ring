package com.sht.eurasiaring.service;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.entity.Post;
import com.sht.eurasiaring.repository.CommentRepository;
import com.sht.eurasiaring.utils.DateUtils;
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
    private PostService postService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    //添加留言
    public JsonData save(Comment comment) {
        comment.setCreateTime(DateUtils.dateToString());
        commentRepository.save(comment);
        Post post = postService.findUserIdByPostId(comment.getPostId());
        if (!comment.getUserId().equals(post.getUserId())) {
            this.redisTemplate.boundValueOps("eurasia_" + post.getUserId()).increment(1);
        }
        return JsonData.buildSuccess("成功");
    }

    //删除留言和留言的回复
    public JsonData delete(Integer id) {
        int num = replyService.deleteByCommentId(id);
        commentRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功,并删除"+ num + "条回复内容");
    }

    //根据postId查询留言回复
    public List<Comment> findBypostId(Integer postId, Integer userId){
        List<Comment> commentList = commentRepository.findBypostId(postId);
        for (Comment comment : commentList) {
            comment.setUser(userService.findUserById(comment.getUserId()));
            comment.setReplyList(replyService.getTreeReply(comment.getCommentId(), userId));
        }
        return commentList;
    }
}
