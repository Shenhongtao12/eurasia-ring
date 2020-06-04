package com.sht.eurasiaring.service;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.entity.Post;
import com.sht.eurasiaring.entity.Reply;
import com.sht.eurasiaring.repository.CommentRepository;
import com.sht.eurasiaring.repository.PraiseRepository;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
    @Autowired
    private PraiseRepository praiseRepository;

    //添加留言
    public JsonData save(Comment comment) {
        comment.setCreateTime(DateUtils.dateToString());
        comment.setNumber(0);
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
    public List<Comment> findByPostId(Integer postId, Integer userId, String sortName){
        Specification<Comment> spec = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                list.add(criteriaBuilder.equal(root.get("postId"), postId));
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        List<Comment> commentList = commentRepository.findAll(spec, Sort.by(Sort.Direction.DESC, sortName));
        for (Comment comment : commentList) {
            comment.setUser(userService.findUserById(comment.getUserId()));
            comment.setReplyList(replyService.getTreeReply(comment.getCommentId(), userId));
            comment.setState(praiseRepository.findPraiseByTypeAndTypeIdAndUserId("comment", comment.getCommentId(), userId) == null ? "false" : "true");
        }
        return commentList;
    }

    //根据commentId 查询一个留言下的回复
    public Comment findOneComment(Integer id, Integer userId){
        Comment comment = findById(id);
        comment.setReplyList(replyService.getTreeReply(comment.getCommentId(), userId));
        comment.setState(praiseRepository.findPraiseByTypeAndTypeIdAndUserId("comment", comment.getCommentId(), userId) == null ? "false" : "true");
        return comment;
    }

    //查看与我相关，回复留言
    public List<Comment> findByPostIdUserId(Integer postId, Integer userId){
        Specification<Comment> spec = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                list.add(criteriaBuilder.equal(root.get("postId"), postId));
                list.add(criteriaBuilder.notEqual(root.get("userId"), userId));
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return commentRepository.findAll(spec);
    }

    //findById
    public Comment findById(Integer id){
        return commentRepository.findById(id).get();
    }

    //根据postId查找对应的commentNum
    public Integer countByPostId(Integer postId){
        return commentRepository.countByPostId(postId);
    }
}
