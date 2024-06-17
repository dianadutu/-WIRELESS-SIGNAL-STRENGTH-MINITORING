package com.licenta.wireless.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Order(1) // Ne asiguram ca aceasta rulează prima
public class NetworkService  implements CommandLineRunner, Ordered {

   // private String filePath;

   // public NetworkService (String filePath) {
     //   this.filePath = filePath;
    //}

    private final String filePath;

    public NetworkService(@Value("${network.filePath}") String filePath) {
        this.filePath = filePath;
    }

    @Override

    public int getOrder() {
        return 1; // returnează ordinea
    }

    public void run(String... args) throws Exception {
        runService();
    }
    public void runService() throws Exception {
        System.out.println("Începe procesarea...");

        // Detectează dinamic numele interfeței și SSID-ul rețelei curente
        String interfaceName = detectWirelessInterfaceName();
        String currentSSID = getCurrentNetworkSSID();
        if (interfaceName == null) {
            System.err.println("Nu s-a putut detecta interfața wireless.");
            return;
        }

        System.out.println("Numele interfeței wireless detectate: " + interfaceName);


        if (currentSSID == null) {
            System.err.println("Nu este conectat la o rețea wireless.");
        } else {
            System.out.println("SSID-ul rețelei curente: " + currentSSID);
        }

        // Dezactivează și re-activează interfața pentru a forța re-scanarea rețelelor
        toggleNetworkInterface(interfaceName);

        // Dacă aveam un SSID memorat, încearcă să te reconectezi la rețea
        if (currentSSID != null) {
            System.out.println("Reconectare la rețea: " + currentSSID);
            connectToNetwork(currentSSID);
        }


    }

    private String detectWirelessInterfaceName() {
        String command = "netsh wlan show interfaces";
        String output = executeCommandAndGetOutput(command);
        Pattern pattern = Pattern.compile("^\\s*Name\\s*:\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null; // Întoarce null dacă nu se găsește nicio interfață
    }

    private String getCurrentNetworkSSID() {
        String command = "netsh wlan show interfaces";
        String output = executeCommandAndGetOutput(command);
        Pattern pattern = Pattern.compile("SSID\\s*:\\s*(.+?)\\r?\\n", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private void toggleNetworkInterface(String interfaceName) {
        System.out.println("Dezactivare interfață: " + interfaceName);
        // Dezactivează interfața
        executeCommand("netsh interface set interface name=\"" + interfaceName + "\" admin=disabled");

        // Așteaptă puțin pentru dezactivare
        try {
            Thread.sleep(1000); // Așteaptă 1 secunde
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Reactivare interfață: " + interfaceName);
        // Reactivează interfața
        executeCommand("netsh interface set interface name=\"" + interfaceName + "\" admin=enabled");

        // Așteaptă puțin pentru reactivare și inițializare
        try {
            Thread.sleep(1000); // Așteaptă alte 1 secunde
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connectToNetwork(String ssid) {
        String command = String.format("netsh wlan connect name=\"%s\" ssid=\"%s\"", ssid, ssid);
        System.out.println("Încercarea de reconectare la rețea: " + command);
        executeCommand(command);

        // Așteaptă un timp pentru a permite reconectarea la rețea
        try {
            System.out.println("Așteptarea reconectării la rețea...");
            Thread.sleep(5000); // Așteaptă 5 secunde pentru reconectare
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verifică dacă reconectarea a avut succes și execută wlanreport
        System.out.println("Generarea raportului WLAN după reconectare...");
        executeCommand("netsh wlan show wlanreport");
    }

    private void executeCommand(String command) {
        System.out.println("Execută comanda: " + command);

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            new BufferedReader(new InputStreamReader(process.getInputStream())).lines().forEach(System.out::println);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String executeCommandAndGetOutput(String command) {
        StringBuilder output = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
