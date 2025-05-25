package com.example.transaction.controller;

import com.example.transaction.dto.UserDto;
import com.example.transaction.entity.Users;
import com.example.transaction.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    // 생성자 주입
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 폼 페이지 이동
    @GetMapping("/signup")
    public String signupForm() {
        return "signup"; // signup.mustache
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(UserDto userDto) {
        System.out.println("UserDto = " + userDto);
        userService.register(userDto);
        return "redirect:/";
    }

    // 로그인 폼 페이지 이동
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.mustache
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Users user = userService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("user", user); // 세션 저장
            return "redirect:/";
        } else {
            model.addAttribute("error", "로그인 실패");
            return "login";
        }
    }

    // 마이페이지 (잔액 확인용)
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage"; // mypage.mustache
    }

    // 돈 충전 폼
    @GetMapping("/charge")
    public String chargeForm() {
        return "charge"; // charge.mustache
    }

    // 돈 충전 처리
    @PostMapping("/charge")
    public String charge(@RequestParam int amount, HttpSession session) {
        Users user = (Users) session.getAttribute("user");

        // 충전 후 최신 사용자 정보 받기
        Users updatedUser = userService.charge(user.getId(), amount);
        if (updatedUser != null) {
            session.setAttribute("user", updatedUser); // 세션 갱신
        }

        return "redirect:/seller/mypage";
    }

    @GetMapping("/change")
    public String exchangeForm(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user); // ✅ Mustache가 이걸 요구함
        return "exchange";
    }

    @PostMapping("/change")
    public String exchange(@RequestParam int amount,
                           @RequestParam String bank,
                           @RequestParam String accountNumber,
                           HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");

        if (amount <= 0) {
            model.addAttribute("error", "잘못된 금액입니다.");
            model.addAttribute("user", user);
            return "exchange";
        }

        Users updatedUser = userService.exchange(user.getId(), amount);

        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
            model.addAttribute("success", "환전이 완료되었습니다.");

            // ✅ 은행과 계좌번호는 여기서 사용만 하고 저장 X
            System.out.println("환전 요청: " + bank + " / " + accountNumber); // 관리자 확인 용
            // 또는: 관리자에게 알림 보내기, 이메일 발송 등
        } else {
            model.addAttribute("error", "보유한 마일리지가 부족합니다.");
        }

        model.addAttribute("user", updatedUser != null ? updatedUser : user);
        return "exchange";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
