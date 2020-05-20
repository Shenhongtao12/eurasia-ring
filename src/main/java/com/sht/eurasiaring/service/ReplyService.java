package com.sht.eurasiaring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sht.eurasiaring.repository.ReplyRepository;
import com.sht.eurasiaring.entity.Reply;
import com.sht.eurasiaring.exception.AllException;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.JpaUtils;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;


    public int deleteByCommentId(Long comid) {
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
            //this.replyRepository.updateLeaf(reply.getLeaf());
            reply.setParentId(reply.getLeaf());
        }
        reply.setParentId(reply.getLeaf());
        reply.setLeaf(0L);
//        int i = this.replyRepository.insertSelective(reply);
        Optional<Reply> originalReply = replyRepository.findById(reply.getId());
        if (originalReply.isPresent()) {
            JpaUtils.copyNotNullProperties(reply, originalReply.get());
        }
        replyRepository.save(reply);

        if (!reply.getUserId().equals(reply.getNameId())) {
            //this.redisTemplate.boundValueOps(String.valueOf(reply.getNameid())).increment(1);
        }
        return JsonData.buildSuccess("回复成功");
    }

    public JsonData delete(Long id) {
        this.replyRepository.deleteById(id);
        return JsonData.buildSuccess("成功");
    }


    //查看与我相关
    /*public JsonData findAllByUser(Integer userId) {
        List<MessageUtils> list = this.replyDao.findReply(userId);
        for (MessageUtils messageUtils : list) {
            MessageUtils goods = this.replyDao.findGoods(messageUtils.getGoodsid());
            String[] images = goods.getImages().split(",");
            messageUtils.setImages(images[0]);
            messageUtils.setName(goods.getName());
        }

        //将留言装进集合
        List<MessageUtils> commentList = new ArrayList<>();
        int[] ids = this.replyDao.findGoodsId(userId);
        for (int i = 0; i < ids.length; i++) {
            int id = ids[i];
            commentList = this.replyDao.findComment(id, userId);
            for (MessageUtils messageUtils : commentList) {
                MessageUtils goods = this.replyDao.findGoods(messageUtils.getGoodsid());
                String[] images = goods.getImages().split(",");
                messageUtils.setImages(images[0]);
                messageUtils.setName(goods.getName());
            }
        }
        if (commentList.size() > 0) {
            list.addAll(commentList);
        }
        //将关注装进集合
        List<MessageUtils> fansList = replyDao.findFans(userId);
        if (fansList.size() > 0){
            list.addAll(fansList);
        }
        //将点赞信息装进集合
        List<MessageUtils> loveMList = new ArrayList<>();
        List<Love> loveList = replyDao.findLove(userId);
        for (Love love : loveList) {
            MessageUtils type = new MessageUtils();
            if ("comment".equals(love.getType())){
                type = replyDao.findCommentContent(love.getTypeid());
            }else if("reply".equals(love.getType())){
                type = replyDao.findReplyContent(love.getTypeid());
            }
            type.setUserid(love.getUserid());
            type.setUser(love.getUser());
            type.setCreatetime(love.getCreatetime());
            MessageUtils goods = this.replyDao.findGoods(type.getGoodsid());
            String[] images = goods.getImages().split(",");
            type.setImages(images[0]);
            loveMList.add(type);
        }
        if (loveMList.size() > 0){
            list.addAll(loveMList);
        }
        //返回总数据
        if (list.size() == 0) {
            return JsonData.buildSuccess("无数据");
        }else {
            Collections.sort(list);
            redisTemplate.delete(String.valueOf(userId));
            redisTemplate.delete("fans-" + userId);
            return JsonData.buildSuccess(list, "");
        }
    }*/


    //树形结构的留言回复数据
    public List<Reply> getTreeReply(Long id, Long userid) {
        List<Reply> list = this.replyRepository.findByCommentId(id);
        for (Reply reply : list) {
            //判断是否对回复点赞
//            reply.setState(this.likeMapper.findLoveBy("reply", reply.getId(), userid));
//            //设置nickname
//            reply.setParentname(replyDao.findNickname(reply.getNameid()));
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
