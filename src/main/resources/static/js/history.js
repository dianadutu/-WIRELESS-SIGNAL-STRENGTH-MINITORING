document.addEventListener('DOMContentLoaded', () => {
    // pt afisare history page
    const rows = document.querySelectorAll('.clickable-row');

    rows.forEach(row => {
        row.addEventListener('click', () => {
            const details = row.querySelector('.details');
            if (details.style.display === 'none' || details.style.display === '') {
                details.style.display = 'block';
            } else {
                details.style.display = 'none';
            }
        });
    });
    ////// end afisare
});

