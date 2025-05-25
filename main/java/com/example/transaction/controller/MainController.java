package com.example.transaction.controller;

import com.example.transaction.entity.Item;
import com.example.transaction.entity.Users;
import com.example.transaction.repository.ItemRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class MainController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/")
    public String main(HttpServletRequest request, Model model) {
        Object user = request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "main";
    }

    @GetMapping("/login-as/{id}")
    public String loginAsTestUser(@PathVariable Long id, HttpServletRequest request) {
        request.getSession().setAttribute("userId", id);
        request.getSession().setAttribute("username", "testseller" + id);
        return "redirect:/";
    }

    @GetMapping("/display-case")
    public String displayCase(HttpServletRequest request, Model model) {
        Users user = (Users) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        List<Item> itemList = itemRepository.findAll();
        List<Item> targetItemList = new ArrayList<>();
        for (Item item : itemList) {
            if (item.isOnsale()) {
                targetItemList.add(item);
            }
        }
        model.addAttribute("itemList", targetItemList);
        return "displayCase";
    }

    @GetMapping("/seller/mypage")
    public String myPage(HttpServletRequest request, Model model) {
        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        List<Item> myItems = itemRepository.findBySellerId(user.getId()).stream()
                .filter(item -> item.getBuyerId() == null)
                .collect(Collectors.toList());

        List<Item> purchasedItems = itemRepository.findByBuyerId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("itemList", myItems);
        model.addAttribute("purchasedList", purchasedItems);

        return "mypage";
    }
}
