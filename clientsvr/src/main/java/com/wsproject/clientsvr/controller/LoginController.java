package com.wsproject.clientsvr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wsproject.clientsvr.annotation.SocialUser;
import com.wsproject.clientsvr.domain.User;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/loginSuccess")
    public String loginComplete(@SocialUser User user, Model model) {
		model.addAttribute("userName", user.getName());
		model.addAttribute("socialType", user.getSocialType().getValue());
		model.addAttribute("admin", user.getRoleType().getValue());
        return "loginSuccess";
    }
}
