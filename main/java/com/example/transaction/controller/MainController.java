package com.example.transaction.controller;

import com.example.transaction.entity.Item;
import com.example.transaction.entity.Users;
import com.example.transaction.repository.ItemRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class MainController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/")
    public String main(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "main";
    }

    @GetMapping("/display-case")
    public String displayCase(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        List<Item> itemList = itemRepository.findAll();
        List<Item> targetItemList = itemList.stream()
                .filter(Item::isOnsale)
                .collect(Collectors.toList());
        model.addAttribute("itemList", targetItemList);
        return "displayCase";
    }

    @GetMapping("/seller/mypage")
    public String myPage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
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
