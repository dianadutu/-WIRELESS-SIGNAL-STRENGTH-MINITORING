document.addEventListener('DOMContentLoaded', function() {
    fetch('/networks')
        .then(response => response.json())
        .then(networkInfos => {

            const container = document.getElementById('networksContainer');
            networkInfos.forEach(network => {
                const networkDiv = document.createElement('div');
                networkDiv.classList.add('network');
                networkDiv.innerHTML = `<strong>SSID ${network.ssid}:</strong><br>
                                        Network Type: ${network.networkType}<br>
                                        Authentication: ${network.authentication}<br>
                                        Encryption: ${network.encryption}<br>`;

                network.bssids.forEach(bssid => {
                    const bssidDiv = document.createElement('div');
                    bssidDiv.classList.add('bssid');
                    bssidDiv.innerHTML = `BSSID ${bssid.bssid}:<br>
                                          Signal: ${bssid.signal}<br>
                                          Radio Type: ${bssid.radioType}<br>
                                          Band: ${bssid.band}<br>
                                          Channel: ${bssid.channel}<br>
                                          Connected Stations: ${bssid.ConnectedStations}<br>
                                          Channel Utilization: ${bssid.ChannelUtilization}<br>
                                          Medium Available Capacity: ${bssid.MediumCapacity}<br>
                                          Basic rates (Mbps): ${bssid.Basicrates}<br>
                                          Other rates (Mbps): ${bssid.Otherrates}<br>`;
                    networkDiv.appendChild(bssidDiv);
                });

                container.appendChild(networkDiv);
            });
        })
        .catch(error => console.error('Error fetching networks:', error));
});
