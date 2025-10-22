document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.querySelector('form[name="f"]');
    const errorMessage = document.getElementById('error-message');

    loginForm.addEventListener('submit', function(e) {
        // Basic client-side validation
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        if (!username || !password) {
            e.preventDefault();
            errorMessage.textContent = 'Please enter both username and password.';
            errorMessage.style.display = 'block';
            return false;
        }

        // For demo purposes, show error on failed login
        // In real app, this would be handled by server response
        setTimeout(() => {
            errorMessage.style.display = 'block';
        }, 1000);
    });
});