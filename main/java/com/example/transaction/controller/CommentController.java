package com.example.transaction.controller;

import com.example.transaction.dto.CommentForm;
import com.example.transaction.entity.Users;
import com.example.transaction.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/create")
    public String createComment(CommentForm commentForm, HttpServletRequest request) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Long itemId = commentService.createComment(user, commentForm);

        return "redirect:/item/" + itemId;
    }


    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id, RedirectAttributes rttr){
        Long targetId = commentService.deleteComment(id, rttr);
        return "redirect:/item/"+ targetId;
    }
}
