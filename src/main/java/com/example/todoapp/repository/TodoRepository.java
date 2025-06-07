package com.example.todoapp.repository;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
	List<Todo> findByUserOrderByCreatedAtDesc(User user);

	List<Todo> findByUserAndCompletedOrderByCreatedAtDesc(User user, boolean completed);
}