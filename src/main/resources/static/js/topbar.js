document.addEventListener('DOMContentLoaded', () => {
    const refreshButton = document.getElementById('refreshButton');
    const historyButton = document.getElementById('historyButton');
    const moreButton = document.getElementById('moreButton');
    const loginButton = document.getElementById('loginButton');
    const logoutButton = document.getElementById('logoutButton');

    // Refresh button functionality
    refreshButton.addEventListener('click', () => {
        location.reload();
    });

    // History button functionality
    historyButton.addEventListener('click', () => {
        alert('History button clicked!');
        // Implement your history functionality here
    });

    // Login/Logout functionality
    let loggedIn = false;

    const updateLoginStatus = () => {
        if (loggedIn) {
            loginButton.style.display = 'none';
            logoutButton.style.display = 'inline-block';
        } else {
            loginButton.style.display = 'inline-block';
            logoutButton.style.display = 'none';
        }
    };

    loginButton.addEventListener('click', () => {
        loggedIn = true;
        updateLoginStatus();
        alert('Logged in!');
        // Implement your login functionality here
    });

    logoutButton.addEventListener('click', () => {
        loggedIn = false;
        updateLoginStatus();
        alert('Logged out!');
        // Implement your logout functionality here
    });

    // Initialize the correct login/logout button state
    updateLoginStatus();

    // Redirecționează către pagina 'networks' doar atunci când butonul "More Info" este apăsat
    moreButton.addEventListener('click', (event) => {
        event.stopPropagation(); // Opresc propagarea evenimentului pentru a preveni acțiuni suplimentare pe elemente părinte
        window.location.href = 'networks';
    });
});
