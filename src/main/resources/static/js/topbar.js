document.addEventListener('DOMContentLoaded', () => {
    const homeButton = document.getElementById('homeButton');
    const refreshButton = document.getElementById('refreshButton');
    const historyButton = document.getElementById('historyButton');
    const moreButton = document.getElementById('moreButton');
    const loginButton = document.getElementById('loginButton');
    const logoutButton = document.getElementById('logoutButton');
    const deviceHistoryButton = document.getElementById('deviceHistoryButton');
    const userHistoryButton = document.getElementById('userHistoryButton');


    // Refresh button functionality
    refreshButton.addEventListener('click', function() {
        window.location.href = '/refresh';
    });

    // Device History button functionality
    deviceHistoryButton.addEventListener('click', () => {
        window.location.href = '/history'; // Redirecționează către pagina history
    });

    // User History button functionality
    userHistoryButton.addEventListener('click', () => {
        const saveProfileModal = document.getElementById('saveProfileModal');
        saveProfileModal.style.display = 'block';
    });

    // Confirm Save Profiles Button functionality
    const confirmSaveProfilesButton = document.getElementById('confirmSaveProfiles');
    if (confirmSaveProfilesButton) {
        confirmSaveProfilesButton.addEventListener('click', async function() {
            await saveProfiles();
            saveProfileModal.style.display = 'none';
            location.reload();
        });
    }

    // Cancel Save Profiles Button functionality
    const cancelSaveProfilesButton = document.getElementById('cancelSaveProfiles');
    if (cancelSaveProfilesButton) {
        cancelSaveProfilesButton.addEventListener('click', function() {
            saveProfileModal.style.display = 'none';
            window.location.href = '/userhistory';
        });
    }

    // Close modal when clicking outside
    window.onclick = function(event) {
        if (event.target === saveProfileModal) {
            saveProfileModal.style.display = 'none';
        }
    };

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

    // Afisare login/logout corect
    updateLoginStatus();

    // Redirecționează către pagina 'networks' doar atunci când butonul "More Info" este apăsat
    moreButton.addEventListener('click', (event) => {
        event.stopPropagation(); // Opresc propagarea evenimentului pentru a preveni acțiuni suplimentare pe elemente părinte
        window.location.href = '/networks';
    });

    homeButton.addEventListener('click', function() {
        window.location.href = '/home';
    });

    // Function to save profiles
    const saveProfiles = () => {
        const rows = document.querySelectorAll('#profilesTable .clickable-row');

        rows.forEach(row => {
            const profile = {
                approachingDataLimit: row.querySelector('[id^=approachingDataLimit_]').textContent.trim() === 'Yes',
                authCredential: row.querySelector('[id^=authCredential_]') ? row.querySelector('[id^=authCredential_]').textContent.trim() : null,
                authentication: row.querySelector('[id^=authentication_]').textContent.trim(),
                autoSwitch: row.querySelector('[id^=autoSwitch_]').textContent.trim() === 'Yes',
                cacheUserInfo: row.querySelector('[id^=cacheUserInfo_]') ? row.querySelector('[id^=cacheUserInfo_]').textContent.trim() : null,
                cipher: row.querySelector('[id^=cipher_]').textContent.trim(),
                congested: row.querySelector('[id^=congested_]').textContent.trim() === 'Yes',
                connectionMode: row.querySelector('[id^=connectionMode_]').textContent.trim(),
                connectionTime: null,  // Trebuie să gestionăm valoarea null sau să preluăm valoarea corectă
                controlOptions: row.querySelector('[id^=controlOptions_]') ? row.querySelector('[id^=controlOptions_]').textContent.trim() : null,
                cost: row.querySelector('[id^=cost_]') ? row.querySelector('[id^=cost_]').textContent.trim() : null,
                costSource: row.querySelector('[id^=costSource_]') ? row.querySelector('[id^=costSource_]').textContent.trim() : null,
                credentialsConfigured: row.querySelector('[id^=credentialsConfigured_]') ? row.querySelector('[id^=credentialsConfigured_]').textContent.trim() === 'Yes' : null,
                disconnectionTime: null,  // Trebuie să gestionăm valoarea null sau să preluăm valoarea corectă
                eapType: row.querySelector('[id^=eapType_]') ? row.querySelector('[id^=eapType_]').textContent.trim() : null,
                macRandomization: row.querySelector('[id^=macRandomization_]').textContent.trim(),
                networkBroadcast: row.querySelector('[id^=networkBroadcast_]').textContent.trim(),
                networkType: row.querySelector('[id^=networkType_]').textContent.trim(),
                numberOfSSIDs: parseInt(row.querySelector('[id^=numberOfSSIDs_]').textContent.trim(), 10),
                overDataLimit: row.querySelector('[id^=overDataLimit_]').textContent.trim() === 'Yes',
                profileName: row.querySelector('[id^=profileName_]').textContent.trim(),
                radioType: row.querySelector('[id^=radioType_]').textContent.trim(),
                roaming: row.querySelector('[id^=roaming_]').textContent.trim() === 'Yes',
                securityKeyPresent: row.querySelector('[id^=securityKeyPresent_]').textContent.trim() === 'Yes',
                ssidName: row.querySelector('[id^=ssidName_]').textContent.trim(),
                type: row.querySelector('[id^=type_]').textContent.trim(),
                vendorExtension: row.querySelector('[id^=vendorExtension_]').textContent.trim(),
                version: parseInt(row.querySelector('[id^=version_]').textContent.trim(), 10)
            };

            console.log('Profile to be saved:', profile); // Adăugăm un log pentru profilul ce urmează să fie salvat
            saveProfile(profile);
        });
    };

    const saveProfile = async (profile) => {
        const url = '/history/add/';

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(profile)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            console.log('Profile saved:', data);
        } catch (error) {
            console.error('Error saving profile:', error);
        }
    };
});
