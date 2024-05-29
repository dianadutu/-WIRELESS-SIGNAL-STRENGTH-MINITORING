document.addEventListener('DOMContentLoaded', function() {
    if (typeof networksData !== 'undefined' && networksData.length > 0) { // pt grafic ca nu mergea
        console.log('networksData:', networksData);

        let signal2 = [];
        let signal5 = [];

        for (let j = 0; j < networksData[0].bssids.length; j++) {
            if (networksData[0].bssids[j].band === "2.4 GHz") {
                let data = {
                    channel: networksData[0].bssids[j].channel,
                    signal: parseFloat(networksData[0].bssids[j].signal),
                    ssid: networksData[0].ssid
                };
                signal2.push(data);
            }
            if (networksData[0].bssids[j].band === "5 GHz") {
                let data = {
                    channel: networksData[0].bssids[j].channel,
                    signal: parseFloat(networksData[0].bssids[j].signal),
                    ssid: networksData[0].ssid
                };
                signal5.push(data);
            }
        }
        // Inversăm ordinea semnalelor pentru a afișa în ordine descrescătoare pe axa Y
        signal2.reverse();
        signal5.reverse();

        console.log("SIGNAL2: ", signal2);
        console.log("SIGNAL5: ", signal5);

        console.log("SIGNAL2 SSIDS : ", signal2.map(network => network.ssid));

        // Funcție pentru obținerea culorilor în funcție de valoare
        function getColorForValue(value) {
            if (value > -60) return 'rgba(0, 128, 0, 0.6)';
            else if (value > -70) return 'rgba(75, 192, 192, 0.6)';
            else if (value > -80) return 'rgba(255, 159, 64, 0.6)';
            else return 'rgba(255, 99, 132, 0.6)';
        }

        // Funcție pentru crearea zonelor de adnotare
        function createAnnotationZones() {
            return {
                zoneRed: {
                    type: 'box',
                    yMin: -80, // Inversăm valorile pentru a începe de sus (-90)
                    yMax: -90, // Inversăm valorile pentru a începe de sus (-90)
                    backgroundColor: 'rgba(255, 99, 132, 0.25)',
                    borderWidth: 0,
                },
                zoneOrange: {
                    type: 'box',
                    yMin: -70, // Inversăm valorile pentru a începe de sus (-90)
                    yMax: -80, // Inversăm valorile pentru a începe de sus (-90)
                    backgroundColor: 'rgba(255, 159, 64, 0.25)',
                    borderWidth: 0,
                },
                zoneLightGreen: {
                    type: 'box',
                    yMin: -60, // Inversăm valorile pentru a începe de sus (-90)
                    yMax: -70, // Inversăm valorile pentru a începe de sus (-90)
                    backgroundColor: 'rgba(75, 192, 192, 0.25)',
                    borderWidth: 0,
                },
                zoneGreen: {
                    type: 'box',
                    yMin: -30, // Inversăm valorile pentru a începe de sus (-90)
                    yMax: -60, // Inversăm valorile pentru a începe de sus (-90)
                    backgroundColor: 'rgba(0, 128, 0, 0.25)',
                    borderWidth: 0,
                }
            };
        }

        // Opțiuni comune pentru ambele grafice
        var commonOptions = {
            scales: {
                y: {
                    min: -90, // Noua valoare minimă a axei Y este -90
                    max: -30, // Noua valoare maximă a axei Y este -30
                    ticks: {
                        stepSize: 10 // Pasul este de 10 dBm
                    }
                }
            },
            plugins: {
                annotation: {
                    annotations: createAnnotationZones()
                }
            }
        };

        // Opțiuni specifice pentru graficul de 2.4GHz
        var chartOptions2GHz = {
            ...commonOptions,
            scales: {
                ...commonOptions.scales,
                x: {
                    type: 'linear',
                    position: 'bottom',
                    min: 1,
                    max: 13,
                    ticks: {
                        stepSize: 1,
                        callback: function (value, index) {
                            return value.toFixed(0);
                        }
                    }
                }
            }
        };

        // Opțiuni specifice pentru graficul de 5GHz
        var chartOptions5GHz = {
            ...commonOptions,
            scales: {
                ...commonOptions.scales,
                x: {
                    labels: [ 36, 40, 44, 50, 56, 64, "", "", 100, 108, 114, 120, 128, 136, 144, 153, 161],
                    ticks: {}
                }
            }
        };

        // Creează graficul pentru 2.4GHz
        if(signal2.length !== 0) {
            var ctx2GHz = document.getElementById('chart2GHz').getContext('2d');
            var chart2GHz = new Chart(ctx2GHz, {
                type: 'bar', // Schimbă tipul graficului la 'bar'
                data: {
                    labels: signal2.map(network => network.channel), // Afișează numărul canalului pe axa X
                    datasets: [{
                        label: 'Semnal 2.4GHz',
                        data: signal2.map(network => parseFloat(network.signal)), // Convertim semnalul la float
                        backgroundColor: signal2.map(network => getColorForValue(parseFloat(network.signal))), // Convertim semnalul la float pentru a obține culoarea
                        borderColor: 'rgba(0,0,0,1)',
                        borderWidth: 1
                    }]
                },
                options: chartOptions2GHz
            });
        }
        // Creează graficul pentru 5GHz
        if(signal5.length !== 0) {
            var ctx5GHz = document.getElementById('chart5GHz').getContext('2d');
            var chart5GHz = new Chart(ctx5GHz, {
                type: 'bar', // Schimbă tipul graficului la 'bar'
                data: {
                    labels: signal5.map(network => network.channel), // Afișează SSID-ul pe axa X
                    datasets: [{
                        label: 'Semnal 5GHz',
                        data: signal5.map(network => parseFloat(network.signal)), // Convertim semnalul la float
                        backgroundColor: signal5.map(network => getColorForValue(parseFloat(network.signal))), // Convertim semnalul la float pentru a obține culoarea
                        borderColor: 'rgba(0,0,0,1)',
                        borderWidth: 1
                    }]
                },
                options: chartOptions5GHz
            });
        } else {
            console.error('networksData is undefined or empty.');
        }
    } else {
        console.error('networksData is undefined or empty.');
    }
});

