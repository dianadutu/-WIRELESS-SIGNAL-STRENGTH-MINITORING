let sortOrder = {
    signal: 'asc',
    connectedStations: 'asc',
    channel: 'asc',
    rates: 'asc'
};

function sortColumn(column) {
    const table = document.querySelector('.table-networks tbody');
    const rows = Array.from(table.querySelectorAll('tr'));
    const order = sortOrder[column] === 'asc' ? 1 : -1;

    rows.sort((a, b) => {
        const aText = a.querySelector(`td:nth-child(${getColumnIndex(column)})`).textContent.trim();
        const bText = b.querySelector(`td:nth-child(${getColumnIndex(column)})`).textContent.trim();

        let aValue, bValue;

        if (column === 'channel' || column === 'rates') {
            // Split the channels/rates into arrays of numbers
            aValue = aText.split(',').map(num => parseFloat(num.trim()));
            bValue = bText.split(',').map(num => parseFloat(num.trim()));

            // First compare by the number of channels/rates
            if (aValue.length < bValue.length) {
                return -1 * order;
            } else if (aValue.length > bValue.length) {
                return 1 * order;
            } else {
                // If the number of channels/rates is the same, compare numerically
                for (let i = 0; i < aValue.length; i++) {
                    if (aValue[i] < bValue[i]) {
                        return -1 * order;
                    } else if (aValue[i] > bValue[i]) {
                        return 1 * order;
                    }
                }
                return 0; // They are equal
            }
        } else if (column === 'connectedStations' || column === 'signal') {
            aValue = aText === '-' ? 0 : parseFloat(aText);
            bValue = bText === '-' ? 0 : parseFloat(bText);
        } else {
            aValue = isNaN(aText) ? aText : parseFloat(aText);
            bValue = isNaN(bText) ? bText : parseFloat(bText);
        }

        if (aValue < bValue) {
            return -1 * order;
        } else if (aValue > bValue) {
            return 1 * order;
        } else {
            return 0;
        }
    });

    while (table.firstChild) {
        table.removeChild(table.firstChild);
    }

    table.append(...rows);
    sortOrder[column] = sortOrder[column] === 'asc' ? 'desc' : 'asc';
}

function getColumnIndex(column) {
    switch (column) {
        case 'signal':
            return 2;
        case 'connectedStations':
            return 3;
        case 'channel':
            return 4;
        case 'rates':
            return 5;
        default:
            return 1;
    }
}

function networkRowClick(row) {
    var index = row.rowIndex - 1;
    var url = '/network/' + encodeURIComponent(index);
    window.location.href = url;
}

function toggleAnswer(id) {
    const answer = document.getElementById(id);
    if (answer.style.display === 'block') {
        answer.style.display = 'none';
    } else {
        answer.style.display = 'block';
    }
}

