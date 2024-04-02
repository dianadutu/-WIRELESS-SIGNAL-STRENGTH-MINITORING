document.addEventListener('DOMContentLoaded', function() {
    fetch('/networks')
        .then(response => response.json())
        .then(networkInfos => {
            const table = document.getElementById('networksTable');
            networkInfos.forEach(network => {
                const row = table.insertRow();
                let cell = row.insertCell(0);
                cell.textContent = network.ssid;
                cell = row.insertCell(1);
                cell.textContent = network.networkType;
                cell = row.insertCell(2);
                cell.textContent = network.authentication;
                cell = row.insertCell(3);
                cell.textContent = network.encryption;

                // Pentru fiecare BSSID, adăugăm un nou rând sub SSID-ul său
                network.bssids.forEach(bssidInfo => {
                    const bssidRow = table.insertRow();
                    bssidRow.insertCell(0); // Celulă goală pentru decalaj
                    let bssidCell = bssidRow.insertCell(1);
                    bssidCell.textContent = `BSSID: ${bssidInfo.bssid}, Signal: ${bssidInfo.signal}, ...`; // Completează cu restul detaliilor
                    // Adaugă mai multe celule pentru restul detaliilor despre BSSID
                });
            });
        })
        .catch(error => console.error('Error fetching networks:', error));
});
