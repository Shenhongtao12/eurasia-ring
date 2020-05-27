package com.sht.eurasiaring.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.entity.Post;
import com.sht.eurasiaring.entity.Praise;
import com.sht.eurasiaring.repository.CommentRepository;
import com.sht.eurasiaring.repository.PraiseRepository;
import com.sht.eurasiaring.repository.ReplyRepository;
import com.sht.eurasiaring.entity.Reply;
import com.sht.eurasiaring.exception.AllException;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.JsonData;
import com.sht.eurasiaring.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;


@Service
@Transactional
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private PraiseRepository praiseRepository;
    @Autowired
    private CommentService commentService;


    public int deleteByCommentId(Integer comid) {
        return replyRepository.deleteByCommentId(comid);
    }


    public JsonData save(Reply reply) throws Exception {
        if (reply.getCommentId() == null || reply.getLeaf() == null) {
            throw new AllException(-1, "回复失败");
        }
        if (reply.getUserId() == null) {
            throw new AllException(-1, "请登录！");
        }
        if (reply.getContent().equals("") || reply.getContent().equals(" ")) {
            throw new AllException(-1, "内容不能为空");
        }
        if (reply.getContent().getBytes("UTF-8").length > 200) {
            throw new AllException(-1, "内容过长");
        }
        reply.setCreateTime(DateUtils.dateToString());

        if (reply.getLeaf() != 0) {
            Reply reply1 = replyRepository.findById(reply.getLeaf()).get();
            reply1.setLeaf(1);
            replyRepository.save(reply1);
        }
        reply.setParentId(reply.getLeaf());
        reply.setLeaf(0);
        System.out.println("回复留言: " + reply);
        replyRepository.save(reply);
        /*Optional<Reply> originalReply = replyRepository.findById(reply.getId());
        if (originalReply.isPresent()) {
            JpaUtils.copyNotNullProperties(reply, originalReply.get());
        }*/

        if (!reply.getUserId().equals(reply.getNameId())) {
            this.redisTemplate.boundValueOps("eurasia_"+reply.getNameId()).increment(1);
        }
        return JsonData.buildSuccess("回复成功");
    }

    public JsonData delete(Integer id) {
        this.replyRepository.deleteById(id);
        return JsonData.buildSuccess("成功");
    }


    //查看与我相关
    public JsonData findAllByUser(Integer userId) {
        List<MessageUtils> messageUtilsList = new ArrayList<>();
        //自定义查询条件
        Specification<Reply> spec = new Specification<Reply>() {
            @Override
            public Predicate toPredicate(Root<Reply> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                //Path<Reply> path = root.get("nameId");
                //相当于 where receiverName = "Veggie", CriteriaBuilder接口中还有很多查询条件，建议看源码
                //Predicate equal = criteriaBuilder.equal(path, userId);
                list.add(criteriaBuilder.equal(root.get("nameId"), userId));
                list.add(criteriaBuilder.notEqual(root.get("userId"), userId));
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        List<Reply> list =replyRepository.findAll(spec);
        for (Reply reply : list) {
            Post post = postService.findPostById(reply.getPostId());
            String[] images = post.getImagesUrl().split(",");

            MessageUtils message = new MessageUtils();
            message.setCreateTime(reply.getCreateTime());
            message.setContent(reply.getContent());
            message.setPostId(reply.getPostId());
            message.setImages(images[0]);
            message.setName(post.getTitle());
            message.setUser(userService.findUserById(reply.getUserId()));
            messageUtilsList.add(message);
        }


        //将留言装进集合
        List<Post> postList = postService.findByUserId(userId);
        List<Comment> commentList;
        for (Post post : postList) {
            commentList = commentService.findByPostIdUserId(post.getId(), userId);
            for (Comment comment : commentList) {
                MessageUtils message = new MessageUtils();
                message.setName(post.getTitle());
                message.setImages(post.getImagesUrl().split(",")[0]);
                message.setContent(comment.getContent());
                message.setCreateTime(comment.getCreateTime());
                message.setPostId(comment.getPostId());
                message.setUser(userService.findUserById(comment.getUserId()));
                messageUtilsList.add(message);
            }
        }


       /* //将关注装进集合
        List<MessageUtils> fansList = replyRepository.findFans(userId);
        if (fansList.size() > 0){
            list.addAll(fansList);
        }*/
        //将点赞信息装进集合
        List<Praise> praiseList = praiseRepository.findByTypeUserId(userId);
        for (Praise praise : praiseList) {
            MessageUtils message = new MessageUtils();
            message.setCreateTime(praise.getCreateTime());
            message.setUser(userService.findUserById(praise.getUserId()));
            if ("reply".equals(praise.getType())){
                Reply reply = replyRepository.findById(praise.getTypeId()).get();
                Post post = postService.findPostById(reply.getPostId());
                message.setName(post.getTitle());
                message.setImages(post.getImagesUrl().split(",")[0]);
            }else if("comment".equals(praise.getType())){
                Comment comment = commentService.findById(praise.getTypeId());
                Post post = postService.findPostById(comment.getPostId());
                message.setName(post.getTitle());
                message.setImages(post.getImagesUrl().split(",")[0]);
            }
            messageUtilsList.add(message);
        }
        //返回总数据
        if (messageUtilsList.size() == 0) {
            return JsonData.buildSuccess("无数据");
        }else {
            Collections.sort(messageUtilsList);
            redisTemplate.delete(String.valueOf(userId));
            redisTemplate.delete("fans-" + userId);
            return JsonData.buildSuccess(messageUtilsList, "");
        }
    }


    //树形结构的留言回复数据
    public List<Reply> getTreeReply(Integer id, Integer userId) {
        List<Reply> list = this.replyRepository.findByCommentId(id);
        for (Reply reply : list) {
            reply.setUser(userService.findUserById(reply.getUserId()));
            //判断是否对回复点赞
            reply.setState(praiseRepository.findPraiseByTypeAndTypeIdAndUserId("reply", reply.getId(), userId) == null ? "false" : "true");
        }

        connectReply(list);

        List<Reply> rootReply = getRootReply(list);

        List<Reply> result = new ArrayList<Reply>();


        for (Reply reply : rootReply) {
            addReplyToResult(result, reply);
        }
        return result;
    }


    private void connectReply(List<Reply> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            Reply replyLast = (Reply) list.get(i);
            List<Reply> replyList = new ArrayList<Reply>();
            List<Reply> rList = new ArrayList<Reply>();
            for (int j = i + 1; j < list.size(); j++) {
                Reply replyNext = (Reply) list.get(j);
                if (replyNext.getParentId().equals(replyLast.getId())) {
                    replyList.add(replyNext);
                } else if (replyNext.getId().equals(replyLast.getParentId())) {
                    rList.add(replyLast);
                    replyNext.setReplyList(rList);
                }
            }
            replyLast.setReplyList(replyList);
        }
    }


    private List<Reply> getRootReply(List<Reply> list) {
        List<Reply> rootReply = new ArrayList<Reply>();
        for (Reply reply : list) {
            if (reply.getParentId() == 0) {
                rootReply.add(reply);
            }
        }
        return rootReply;
    }


    private void addReplyToResult(List<Reply> result, Reply reply) {
        result.add(reply);
        if (reply.getLeaf() == 0) {
            return;
        }
        List<Reply> list = reply.getReplyList();
        for (Reply reply1 : list) {
            if (reply1.getParentId() == 0)
                addReplyToResult(result, reply1);
        }
    }
}
