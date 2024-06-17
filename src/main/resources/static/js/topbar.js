document.addEventListener('DOMContentLoaded', () => {
    const homeButton = document.getElementById('homeButton');
    const refreshButton = document.getElementById('refreshButton');
    const historyButton = document.getElementById('historyButton');
    const moreButton = document.getElementById('moreButton');
    const loginButton = document.getElementById('loginButton');
    const logoutButton = document.getElementById('logoutButton');

    // Refresh button functionality
    document.getElementById('refreshButton').addEventListener('click', function() {
        window.location.href = '/refresh';
    });

    // History button functionality
    historyButton.addEventListener('click', () => {
        window.location.href = '/history'; // Redirecționează către pagina history
    });

    // Verificăm starea de autentificare din local storage
    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';

    const updateLoginStatus = () => {
        if (isLoggedIn) {
            loginButton.style.display = 'none';
            logoutButton.style.display = 'inline-block';
        } else {
            loginButton.style.display = 'inline-block';
            logoutButton.style.display = 'none';
        }
    };

    loginButton.addEventListener('click', () => {
        localStorage.setItem('isLoggedIn', 'true');
        updateLoginStatus();
        window.location.href = '/login';
    });

    logoutButton.addEventListener('click', () => {
        localStorage.setItem('isLoggedIn', 'false');
        updateLoginStatus();
        window.location.href = '/logout';
    });

    // afisare login/logout corect
    updateLoginStatus();

    // Redirecționează către pagina 'networks' doar atunci când butonul "More Info" este apăsat
    moreButton.addEventListener('click', (event) => {
        event.stopPropagation(); // Opresc propagarea evenimentului pentru a preveni acțiuni suplimentare pe elemente părinte
        window.location.href = '/networks';
    });

    document.getElementById('moreButton').addEventListener('click', function() {
        window.location.href = '/networks';
    });

    document.getElementById('homeButton').addEventListener('click', function() {
        window.location.href = '/home';
    });
});
