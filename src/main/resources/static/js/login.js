document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const hashedPassword = await hashPassword(password);

    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                email: email,
                password: hashedPassword,
            }),
        });

        if (response.ok) {
            const data = await response.json();
            if (data.success) {
                // Stocăm starea de autentificare
                localStorage.setItem('isLoggedIn', 'true');
                window.location.href = data.redirectUrl || '/home.html';
            } else {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: data.message || "Something went wrong!",

                });
            }
        } else {
            const data = await response.json();
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: data.message || "Something went wrong!",

            });
        }
    } catch (error) {
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: 'An error occurred while logging in. Please try again.',

        });
    }
});

async function hashPassword(password) {
    const passwordBuffer = new TextEncoder().encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', passwordBuffer);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');
    return hashHex;
}
