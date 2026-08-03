const loginForm = document.getElementById('loginForm');

if (loginForm) {
    loginForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const errorMessage = document.getElementById('errorMessage');
        errorMessage.textContent = '';

        try {
            const data = await apiRequest('/auth/login', 'POST', { email, password });
            localStorage.setItem('token', data.token);
            window.location.href = 'dashboard.html';
        } catch (error) {
            errorMessage.textContent = error.message;
        }
    });
}

const registerForm = document.getElementById('registerForm');

if (registerForm) {
    registerForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const errorMessage = document.getElementById('errorMessage');
        const successMessage = document.getElementById('successMessage');
        errorMessage.textContent = '';
        successMessage.textContent = '';

        try {
            await apiRequest('/auth/register', 'POST', { name, email, password });
            successMessage.textContent = 'Registration successful! Redirecting to login...';
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1500);
        } catch (error) {
            errorMessage.textContent = error.message;
        }
    });
}