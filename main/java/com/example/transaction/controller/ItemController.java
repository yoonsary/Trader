package com.example.transaction.controller;

import com.example.transaction.dto.ItemForm;
import com.example.transaction.entity.Item;
import com.example.transaction.entity.Users;
import com.example.transaction.service.CommentService;
import com.example.transaction.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    public ItemController(ItemService itemService, CommentService commentService) {

        this.itemService = itemService;
        this.commentService = commentService;
    }

    @GetMapping("/item/{id}")
    public String itemDetail(@PathVariable("id") Long itemId, Model model, HttpServletRequest request) {
        Users user = (Users) request.getSession().getAttribute("user");
        Long userId = user != null ? user.getId() : 0L;

        if (user != null) {
            model.addAttribute("user", user);
        }

        // 아이템 탐색
        Map<String, Object> itemData = itemService.processItem(itemId, "owner", userId);

        // 댓글 탐색
        List<Map<String, Object>> targetCommentList = commentService.searchComment(itemId, userId);

        model.addAttribute("item", itemData);
        model.addAttribute("comments", targetCommentList);
        return "item/itemDetail";
    }

    @GetMapping("/item/create")
    public String itemCreateForm(HttpServletRequest request, Model model) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userId", user.getId());
            return "item/itemCreateForm";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/item/create")
    public String itemCreate(ItemForm itemForm, RedirectAttributes rttr) {
        if (itemForm.getName() == null || itemForm.getName().trim().isEmpty() ||
                itemForm.getDescription() == null || itemForm.getDescription().trim().isEmpty() ||
                itemForm.getPrice() <= 0) {

            rttr.addFlashAttribute("msg", "모든 항목을 올바르게 입력해주세요.");
            return "redirect:/item/create";
        }

        itemService.saveNewItem(itemForm);

        return "redirect:/seller/mypage";
    }

    @GetMapping("/item/update/{id}")
    public String itemUpdateForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user != null) {
            Item itemEntity = itemService.searchItem(id);
            model.addAttribute("item", itemEntity);
            model.addAttribute("user", user);

            return "item/itemUpdateForm";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/item/update")
    public String itemEdit(ItemForm itemForm) {
        Item target = itemService.updateItem(itemForm);

        return "redirect:/item/" + target.getId();
    }

    @GetMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id, RedirectAttributes rttr, HttpServletRequest request) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user != null) {
            commentService.deleteItemComment(id, rttr);

            itemService.deleteItem(id, rttr);

            return "redirect:/seller/mypage";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/item/sell/{id}")
    public String sellItem(@PathVariable Long id, HttpServletRequest request, RedirectAttributes rttr) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        try{
            itemService.setSellItem(id, true);
            rttr.addFlashAttribute("msg", "판매 등록되었습니다.");
            return "redirect:/seller/mypage";
        }catch(IllegalArgumentException e){
            rttr.addFlashAttribute("msg", "아이템이 존재하지 않습니다.");
            return "redirect:/seller/mypage";
        }
    }

    @GetMapping("/item/cancel/{id}")
    public String cancelSale(@PathVariable("id") Long id, HttpServletRequest request, RedirectAttributes rttr) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/";
        }

        try{
            itemService.setSellItem(id, false);
            rttr.addFlashAttribute("msg", "판매가 취소되었습니다.");
            return "redirect:/display-case";
        }catch(IllegalArgumentException e){
            rttr.addFlashAttribute("msg", "아이템을 찾을 수 없습니다.");
            return "redirect:/display-case";
        }
    }

    @PostMapping("/item/buy/{id}")
    @ResponseBody
    public String buyItem(@PathVariable Long id, HttpServletRequest request) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null) {
            return "로그인이 필요합니다.";
        }

        try{
            itemService.buyItem(id, user);
            return "구매가 완료되었습니다.";
        }catch(IllegalArgumentException e){
            return e.getMessage();
        }
    }
}
