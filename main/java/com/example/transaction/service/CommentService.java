package com.example.transaction.service;

import com.example.transaction.dto.CommentForm;
import com.example.transaction.entity.Comment;
import com.example.transaction.entity.Users;
import com.example.transaction.repository.CommentRepository;
import com.example.transaction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository ;
    private final UserRepository userRepository ;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // 코멘트 생성
    public Long createComment(Users user, CommentForm commentForm){
        // 세션에서 가져온 userId 사용
        Long userId = user.getId();
        Long itemId = commentForm.getItem_id();
        String content = commentForm.getContent();

        Comment comment = new Comment(userId, itemId, content);
        Comment save = commentRepository.save(comment);
        log.info("Saved comment: " + save.toString());
        return itemId;
    }

    // 코멘트 탐색
    public List<Map<String, Object>> searchComment(Long itemId, Long userId){
        List<Comment> commentList = commentRepository.findAll();
        List<Map<String, Object>> targetCommentList = new ArrayList<>();
        for (Comment comment : commentList) {
            if (comment.getItem_id().equals(itemId)) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("user_id", comment.getUser_id());
                commentData.put("item_id", comment.getItem_id());
                commentData.put("content", comment.getContent());

                // 사용자 이름 가져오기
                Users commentUser = userRepository.findById(comment.getUser_id()).orElse(null);
                if (commentUser != null) {
                    commentData.put("username", commentUser.getUsername());
                    commentData.put("nickname", commentUser.getNickname());
                } else {
                    commentData.put("username", "알 수 없음");
                }

                commentData.put("owner", comment.getUser_id() == userId);
                targetCommentList.add(commentData);
            }
        }
        return targetCommentList;
    }

    // 코멘트 삭제
    public void deleteItemComment(Long id, RedirectAttributes rttr){
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            if (comment.getItem_id().equals(id)) {
                commentRepository.deleteById(comment.getId());
                rttr.addFlashAttribute("msg", "삭제되었습니다.");
            }
        }
    }

    public Long deleteComment(Long id, RedirectAttributes rttr){
        // 코멘트 삭제
        Comment target = commentRepository.findById(id).orElse(null);

        if(target != null){
            commentRepository.deleteById(id);
            rttr.addFlashAttribute("msg", "삭제되었습니다.");
        }
        return target.getItem_id();
    }

}
