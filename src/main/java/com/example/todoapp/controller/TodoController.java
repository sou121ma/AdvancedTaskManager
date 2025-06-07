package com.example.todoapp.controller;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.User;
import com.example.todoapp.service.TodoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TodoController {

	private final TodoService todoService;

	@GetMapping("/todos")
	public String showTodos(HttpSession session, Model model, @RequestParam(required = false) String filter) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		List<Todo> todos;

		if ("completed".equals(filter)) {
			todos = todoService.getCompletedTodosByUser(user);
			model.addAttribute("activeFilter", "completed");
		} else if ("active".equals(filter)) {
			todos = todoService.getActiveTodosByUser(user);
			model.addAttribute("activeFilter", "active");
		} else {
			todos = todoService.getTodosByUser(user);
			model.addAttribute("activeFilter", "all");
		}

		model.addAttribute("todos", todos);
		model.addAttribute("newTodo", new Todo());

		return "todos";
	}

	@PostMapping("/todos")
	public String createTodo(@Valid @ModelAttribute("newTodo") Todo todo,
			BindingResult result,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Title is required");
			return "redirect:/todos";
		}

		todo.setUser(user);
		todoService.createTodo(todo);

		return "redirect:/todos";
	}

	@GetMapping("/todos/{id}/edit")
	public String showEditForm(@PathVariable Long id, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		Optional<Todo> todoOptional = todoService.getTodoById(id);

		if (todoOptional.isEmpty() || !todoOptional.get().getUser().getId().equals(user.getId())) {
			return "redirect:/todos";
		}

		model.addAttribute("todo", todoOptional.get());

		return "edit-todo";
	}

	@PostMapping("/todos/{id}")
	public String updateTodo(@PathVariable Long id,
			@Valid @ModelAttribute("todo") Todo todo,
			BindingResult result,
			HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		Optional<Todo> todoOptional = todoService.getTodoById(id);

		if (todoOptional.isEmpty() || !todoOptional.get().getUser().getId().equals(user.getId())) {
			return "redirect:/todos";
		}

		if (result.hasErrors()) {
			return "edit-todo";
		}

		Todo existingTodo = todoOptional.get();
		existingTodo.setTitle(todo.getTitle());
		existingTodo.setDescription(todo.getDescription());
		existingTodo.setPriority(todo.getPriority());
		existingTodo.setDueDate(todo.getDueDate());

		todoService.updateTodo(existingTodo);

		return "redirect:/todos";
	}

	@PostMapping("/todos/{id}/toggle")
	public String toggleTodoCompletion(@PathVariable Long id, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		Optional<Todo> todoOptional = todoService.getTodoById(id);

		if (todoOptional.isEmpty() || !todoOptional.get().getUser().getId().equals(user.getId())) {
			return "redirect:/todos";
		}

		todoService.toggleTodoCompletion(id);

		return "redirect:/todos";
	}


	@PostMapping("/todos/{id}/delete")
	public String deleteTodo(@PathVariable Long id, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		Optional<Todo> todoOptional = todoService.getTodoById(id);

		if (todoOptional.isEmpty() || !todoOptional.get().getUser().getId().equals(user.getId())) {
			return "redirect:/todos";
		}

		todoService.deleteTodo(id);

		return "redirect:/todos";
	}
}