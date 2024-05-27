document.addEventListener('DOMContentLoaded', function() {
    console.log('networksData:', networksData);

    let signal2 = []
    let signal5 = []

    for (let j = 0; j < networksData.bssids.length; j++) {
        if(networksData.bssids[j].band === "2.4 GHz") {
            let data = {
                channel : networksData.bssids[j].channel,
                signal : networksData.bssids[j].signal,
                ssid : networksData.ssid
            }
            signal2.push(data);
        }
        if(networksData.bssids[j].band === "5 GHz") {
            let data = {
                channel : networksData.bssids[j].channel,
                signal : networksData.bssids[j].signal,
                ssid : networksData.ssid
            }
            signal5.push(data);
        }
    }

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

    function createAnnotationZones() {
        return {
            zoneRed: {
                type: 'box',
                yMin: -90,
                yMax: -80,
                backgroundColor: 'rgba(255, 99, 132, 0.25)',
                borderWidth: 0,
            },
            zoneOrange: {
                type: 'box',
                yMin: -80,
                yMax: -70,
                backgroundColor: 'rgba(255, 159, 64, 0.25)',
                borderWidth: 0,
            },
            zoneLightGreen: {
                type: 'box',
                yMin: -70,
                yMax: -60,
                backgroundColor: 'rgba(75, 192, 192, 0.25)',
                borderWidth: 0,
            },
            zoneGreen: {
                type: 'box',
                yMin: -60,
                yMax: -30,
                backgroundColor: 'rgba(0, 128, 0, 0.25)',
                borderWidth: 0,
            }
        };
    }

    var commonOptions = {
        scales: {
            y: {
                beginAtZero: false,
                min: -90,
                max: -30,
                ticks: {
                    stepSize: 10,
                    reverse: false,
                }
            }
        },
        plugins: {
            annotation: {
                annotations: createAnnotationZones()
            }
        }
    };

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

    var chartOptions5GHz = {
        ...commonOptions,
        scales: {
            ...commonOptions.scales,
            x: {
                labels: ["", 36, 40, 44, 50, 56, 64, "", "", 100, 108, 114, 120, 128, 136, 144, 153, 161]
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
    }
});
