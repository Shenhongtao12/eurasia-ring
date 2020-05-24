package com.sht.eurasiaring.service;

import com.sht.eurasiaring.repository.PostRepository;
import com.sht.eurasiaring.entity.Post;
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
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    public PageResult<Post> init(){
        Page<Post> page = postRepository.findAll(PageRequest.of(0, 20));
        return new PageResult<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    public Integer countNumByMatter(Integer matterId){
        return postRepository.countByMatterId(matterId);
    }

    public JsonData save(Post post) {
        if (StringUtils.isEmpty(post.getUserId())){
            return JsonData.buildError("数据错误，未关联用户");
        }
        post.setCreateTime(DateUtils.dateToString());
        postRepository.save(post);
        return JsonData.buildSuccess("成功");
    }

    //暂时不允许更新
    public JsonData update(Post post) {
        Post result = postRepository.findById(post.getId()).get();
        if (StringUtils.isEmpty(result)){
            return JsonData.buildError("数据错误，不存在该postId");
        }
        postRepository.save(post);
        return JsonData.buildSuccess("更新成功");
    }

    public JsonData delete(Integer id) {
        postRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功");
    }

    public Post findById(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId).get();
        post.setCommentList(commentService.findBypostId(postId, userId));
        post.setUser(userService.findUserById(post.getUserId()));
        return post;
    }

    public Post findUserIdByPostId(Integer postId){
        return postRepository.findUserIdById(postId);
    }

    public Post findPostById(Integer postId) {
        return postRepository.findById(postId).get();
    }
}
