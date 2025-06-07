package com.example.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasktodos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Title is required")
	private String title;

	private String description;

	private boolean completed = false;

	@Enumerated(EnumType.STRING)
	private Priority priority = Priority.MEDIUM;

	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public enum Priority {
		LOW, MEDIUM, HIGH
	}
}