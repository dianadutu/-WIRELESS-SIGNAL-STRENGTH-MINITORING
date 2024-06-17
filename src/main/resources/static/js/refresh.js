document.addEventListener("DOMContentLoaded", function () {
    let secondsRemaining = 22; // Numărul total de secunde pentru refresh
    const countdownElement = document.getElementById('seconds-remaining');

    // Funcție pentru actualizarea cronometru
    const updateCountdown = () => {
        countdownElement.textContent = secondsRemaining;
        if (secondsRemaining > 0) {
            secondsRemaining--;
        } else {
            clearInterval(countdownInterval);
        }
    };

    // Actualizează cronometru la fiecare secundă
    const countdownInterval = setInterval(updateCountdown, 1000);

    // Simulează apelul către endpoint-ul de refresh
    fetch('/api/refresh')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                setTimeout(() => {
                    window.location.href = '/home'; // Redirecționează către pagina principală după terminarea refresh-ului
                }, 1000); // Așteaptă 1 secunde înainte de redirecționare
            } else {
                // Manevrarea erorilor
                console.error('Refresh failed');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
