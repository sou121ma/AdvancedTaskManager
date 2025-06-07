package com.example.todoapp.controller;

import com.example.todoapp.model.User;
import com.example.todoapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/signup")
	public String showSignupForm(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/signup")
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "signup";
		}

		try {
			userService.registerUser(user);
			redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			result.rejectValue("username", "error.user", e.getMessage());
			return "signup";
		}
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	@PostMapping("/login")
	public String loginUser(String username, String password,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		Optional<User> userOptional = userService.authenticateUser(username, password);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			session.setAttribute("user", user);
			return "redirect:/todos";
		}

		redirectAttributes.addFlashAttribute("error", "Invalid username or password");
		return "redirect:/login";
	}
}