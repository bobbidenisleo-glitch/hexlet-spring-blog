// DOM ready
document.addEventListener('DOMContentLoaded', function() {
    console.log('Application started');
    
    // Загрузка постов
    loadPosts();
    
    // Обработка форм
    setupFormHandlers();
});

async function loadPosts() {
    try {
        const response = await fetch('/api/posts');
        const posts = await response.json();
        
        const postsContainer = document.getElementById('posts-container');
        if (postsContainer) {
            postsContainer.innerHTML = posts.map(post => `
                <div class="post">
                    <h3 class="post-title">${post.title}</h3>
                    <div class="post-meta">Created: ${new Date(post.createdAt).toLocaleString()}</div>
                    <div class="post-body">${post.body}</div>
                </div>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading posts:', error);
    }
}

function setupFormHandlers() {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
    
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
}

async function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });
        
        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            showMessage('Login successful!', 'success');
            setTimeout(() => {
                window.location.href = '/dashboard.html';
            }, 1000);
        } else {
            showMessage('Login failed!', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('An error occurred', 'error');
    }
}

async function handleRegister(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await fetch('/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, email, password })
        });
        
        if (response.ok) {
            showMessage('Registration successful! Please login.', 'success');
            setTimeout(() => {
                window.location.href = '/login.html';
            }, 1000);
        } else {
            showMessage('Registration failed!', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('An error occurred', 'error');
    }
}

function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    if (messageDiv) {
        messageDiv.textContent = message;
        messageDiv.className = `message ${type}`;
        setTimeout(() => {
            messageDiv.textContent = '';
            messageDiv.className = '';
        }, 3000);
    }
}
