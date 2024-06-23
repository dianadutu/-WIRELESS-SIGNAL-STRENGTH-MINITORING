document.getElementById('create-account-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    document.getElementById('loader').style.display = 'block';
    document.getElementById('error-message').textContent = '';
    document.getElementById('success-message').textContent = '';

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        document.getElementById('loader').style.display = 'none';
        document.getElementById('error-message').textContent = 'Passwords do not match.';
        return;
    }

    try {
        const response = await fetch('/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: new URLSearchParams({
                email: email,
                password: password,
                confirmPassword: confirmPassword
            }),
        });

        const data = await response.json();
        document.getElementById('loader').style.display = 'none';

        if (response.ok && data.success) {
            document.getElementById('success-message').textContent = 'Account created successfully!';
            setTimeout(() => {
                window.location.href = '/login'; // Redirectionare catre login dupa un mic delay
            }, 1000); // redirectionare dupa 1 secunda
        } else {
            const errorMessage = data.message || 'Something went wrong.';
            document.getElementById('error-message').textContent = errorMessage;
        }
    } catch (error) {
        console.error('Error during registration:', error);
        document.getElementById('error-message').textContent = 'An error occurred. Please try again.';
        document.getElementById('loader').style.display = 'none';
    }
});
