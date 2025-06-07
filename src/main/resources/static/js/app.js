document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const todoForm = document.getElementById('todo-form');
    const todoInput = document.getElementById('todo-input');
    const prioritySelect = document.getElementById('priority-select');
    const todoList = document.getElementById('todo-list');
    const filterAll = document.getElementById('filter-all');
    const filterActive = document.getElementById('filter-active');
    const filterCompleted = document.getElementById('filter-completed');
    const clearCompleted = document.getElementById('clear-completed');
    
    // Todo array for non-logged in users
    let todos = JSON.parse(localStorage.getItem('todos')) || [];
    let currentFilter = 'all';
    
    // Initialize
    renderTodos();
    
    // Event Listeners
    if (todoForm) {
        todoForm.addEventListener('submit', addTodo);
    }
    
    if (filterAll) {
        filterAll.addEventListener('click', () => {
            setFilter('all');
        });
    }
    
    if (filterActive) {
        filterActive.addEventListener('click', () => {
            setFilter('active');
        });
    }
    
    if (filterCompleted) {
        filterCompleted.addEventListener('click', () => {
            setFilter('completed');
        });
    }
    
    if (clearCompleted) {
        clearCompleted.addEventListener('click', clearCompletedTodos);
    }
    
    // Functions
    function addTodo(e) {
        e.preventDefault();
        
        const title = todoInput.value.trim();
        if (!title) return;
        
        const todo = {
            id: Date.now(),
            title,
            completed: false,
            priority: prioritySelect.value,
            createdAt: new Date().toISOString()
        };
        
        todos.unshift(todo);
        saveTodos();
        renderTodos();
        
        todoInput.value = '';
    }
    
    function toggleTodo(id) {
        todos = todos.map(todo => {
            if (todo.id === id) {
                return { ...todo, completed: !todo.completed };
            }
            return todo;
        });
        
        saveTodos();
        renderTodos();
    }
    
    function deleteTodo(id) {
        todos = todos.filter(todo => todo.id !== id);
        saveTodos();
        renderTodos();
    }
    
    function clearCompletedTodos() {
        todos = todos.filter(todo => !todo.completed);
        saveTodos();
        renderTodos();
    }
    
    function setFilter(filter) {
        currentFilter = filter;
        
        // Update active button
        [filterAll, filterActive, filterCompleted].forEach(btn => {
            if (btn) btn.classList.remove('active');
        });
        
        if (filter === 'all' && filterAll) {
            filterAll.classList.add('active');
        } else if (filter === 'active' && filterActive) {
            filterActive.classList.add('active');
        } else if (filter === 'completed' && filterCompleted) {
            filterCompleted.classList.add('active');
        }
        
        renderTodos();
    }
    
    function renderTodos() {
        if (!todoList) return;
        
        // Filter todos
        let filteredTodos = todos;
        if (currentFilter === 'active') {
            filteredTodos = todos.filter(todo => !todo.completed);
        } else if (currentFilter === 'completed') {
            filteredTodos = todos.filter(todo => todo.completed);
        }
        
        // Clear list
        todoList.innerHTML = '';
        
        // Show message if no todos
        if (filteredTodos.length === 0) {
            const emptyMessage = document.createElement('li');
            emptyMessage.className = 'list-group-item text-center py-3';
            emptyMessage.innerHTML = `
                <i class="bi bi-clipboard text-muted" style="font-size: 2rem;"></i>
                <p class="mt-2 mb-0 text-muted">No tasks found</p>
            `;
            todoList.appendChild(emptyMessage);
            return;
        }
        
        // Render todos
        filteredTodos.forEach(todo => {
            const li = document.createElement('li');
            li.className = 'list-group-item d-flex justify-content-between align-items-center';
            
            // Determine priority badge color
            let priorityClass = 'bg-info text-dark';
            if (todo.priority === 'HIGH') {
                priorityClass = 'bg-danger';
            } else if (todo.priority === 'MEDIUM') {
                priorityClass = 'bg-warning text-dark';
            }
            
            li.innerHTML = `
                <div class="d-flex align-items-center flex-grow-1">
                    <button class="btn btn-sm btn-outline-secondary rounded-circle me-2 toggle-btn" data-id="${todo.id}">
                        <i class="bi ${todo.completed ? 'bi-check-circle-fill text-success' : 'bi-circle'}"></i>
                    </button>
                    <div class="flex-grow-1">
                        <div class="${todo.completed ? 'text-decoration-line-through text-muted' : ''}">
                            ${todo.title}
                            <span class="badge ms-2 ${priorityClass}">${todo.priority}</span>
                        </div>
                    </div>
                </div>
                <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${todo.id}">
                    <i class="bi bi-trash"></i>
                </button>
            `;
            
            todoList.appendChild(li);
            
            // Add event listeners
            li.querySelector('.toggle-btn').addEventListener('click', () => {
                toggleTodo(todo.id);
            });
            
            li.querySelector('.delete-btn').addEventListener('click', () => {
                deleteTodo(todo.id);
            });
        });
    }
    
    function saveTodos() {
        localStorage.setItem('todos', JSON.stringify(todos));
    }
});