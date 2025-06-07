package com.example.todoapp.service;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.User;
import com.example.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<Todo> getTodosByUser(User user) {
        return todoRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Todo> getCompletedTodosByUser(User user) {
        return todoRepository.findByUserAndCompletedOrderByCreatedAtDesc(user, true);
    }
    
    public List<Todo> getActiveTodosByUser(User user) {
        return todoRepository.findByUserAndCompletedOrderByCreatedAtDesc(user, false);
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    public void toggleTodoCompletion(Long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        }
    }
}