package com.example.transaction.controller;

import com.example.transaction.dto.UserDto;
import com.example.transaction.entity.Users;
import com.example.transaction.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserDto userDto) {
        userService.register(userDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Users user = userService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "로그인 실패");
            return "login";
        }
    }

    @GetMapping("/charge")
    public String chargeForm(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "charge";
    }

    @PostMapping("/charge")
    public String charge(@RequestParam int amount, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Users updatedUser = userService.charge(user.getId(), amount);
        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
        }

        return "redirect:/seller/mypage";
    }

    @GetMapping("/change")
    public String exchangeForm(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "exchange";
    }

    @PostMapping("/change")
    public String exchange(@RequestParam int amount,
                           @RequestParam String bank,
                           @RequestParam String accountNumber,
                           HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (amount <= 0) {
            model.addAttribute("error", "잘못된 금액입니다.");
            model.addAttribute("user", user);
            return "exchange";
        }

        Users updatedUser = userService.exchange(user.getId(), amount);

        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
            model.addAttribute("success", "환전이 완료되었습니다.");
            System.out.println("환전 요청: " + bank + " / " + accountNumber);
        } else {
            model.addAttribute("error", "보유한 마일리지가 부족합니다.");
        }

        model.addAttribute("user", updatedUser != null ? updatedUser : user);
        return "exchange";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
