document.getElementById('sendCodeButton').addEventListener('click', function () {
    const emailToSendCode = document.getElementById('emailToSendCode').value;

    fetch('/send-reset-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: emailToSendCode }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                Swal.fire('Success', 'Reset code sent to your email.', 'success');
                document.getElementById('sendCodeForm').style.display = 'none';
                document.getElementById('resetPasswordForm').style.display = 'block';
            } else {
                Swal.fire('Error', data.message, 'error');
            }
        })
        .catch(error => {
            Swal.fire('Error', 'An error occurred while sending the reset code.', 'error');
            console.error('Error:', error);
        });
});

document.getElementById('resetPasswordForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const resetCode = document.getElementById('resetCode').value;
    const email = document.getElementById('email').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword !== confirmPassword) {
        Swal.fire('Error', 'Passwords do not match.', 'error');
        return;
    }

    fetch('/reset-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            resetCode: resetCode,
            email: email,
            newPassword: newPassword
        }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                Swal.fire('Success', 'Your password has been reset.', 'success')
                    .then(() => {
                        window.location.href = 'login'; // Redirecționare către login.html
                    });
            } else {
                Swal.fire('Error', data.message, 'error');
            }
        })
        .catch(error => {
            Swal.fire('Error', 'An error occurred while resetting your password.', 'error');
            console.error('Error:', error);
        });
});
