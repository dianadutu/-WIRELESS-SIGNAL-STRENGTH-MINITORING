package com.licenta.wireless.util;

import com.licenta.wireless.Model.NetworkInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(2) // Rulează după NetworkService
public class HtmlParserUtil implements CommandLineRunner, Ordered {

    // Injectează valoarea din application.properties
    @Value("${network.filePath}")
    private String filePath;

    @Override
    public void run(String... args) throws Exception {
        parseHtml();
    }

    @Override
    public int getOrder() {
        return 2; // ordinea de execuție
    }

    public List<NetworkInfo> parseHtml() {
        List<NetworkInfo> networkInfos = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Document doc = Jsoup.parse(content);


            //  extragerea titlurilor din document
            Elements titles = doc.select("title");
            for (Element title : titles) {
                System.out.println("Titlu: " + title.text());
            }

            // Se presupune că informațiile despre rețea sunt în interiorul elementelor <textarea>
            Elements textAreas = doc.select("textarea.command-output");
            for (Element textArea : textAreas) {
                // Extrage conținutul fiecărui <textarea>
                String textAreaContent = textArea.text();
                // Adaugă toate rețelele returnate de processNetworkInfo în lista principală
                List<NetworkInfo> extractedInfos = processNetworkInfo(textAreaContent);
                networkInfos.addAll(extractedInfos);


            }

            System.out.println("Procesare fișier HTML completată.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return networkInfos;
    }



        private List<NetworkInfo> processNetworkInfo(String networkInfo) {
            String[] lines = networkInfo.split("\n");
            List<NetworkInfo> networkInfos = new ArrayList<>();
            NetworkInfo network = null; // Nu inițializăm încă
            List<NetworkInfo.BSSIDInfo> bssidInfos = new ArrayList<>();
            NetworkInfo.BSSIDInfo currentBssidInfo = null;

            boolean inTargetSection = false;
            boolean isProcessingBSSID = false;
            //String currentSSID = "";

            // Prelucrare pentru numele interfeței și numărul de rețele
            for (String line : lines) {
                if (line.trim().startsWith("Interface name")) {
                    System.out.println(line.trim()); // Afișează numele interfeței
                    continue; // Trecem la următoarea linie
                }
                if (line.trim().matches("There are \\d+ networks currently visible\\.")) {
                    System.out.println(line.trim()); // Afișează numărul de rețele vizibile
                    System.out.println(); // Spațiu pentru lizibilitate
                    break; // Presupunem că această informație este întotdeauna înaintea listei de SSID-uri și terminăm bucla
                }
            }

            for (String line : lines) {
                String trimmedLine = line.trim();

                // Verificăm începutul secțiunii dorite
                if (trimmedLine.startsWith("======================= SHOW NETWORKS MODE=BSSID ======================")) {
                    inTargetSection = true;
                    continue; // Trecem la următoarea iterație, această linie nu trebuie procesată
                }
                // Verificăm sfârșitul secțiunii dorite
                if (trimmedLine.startsWith("======================= SHOW INTERFACE CAPABILITIES ===================")) {
                    break; // Oprim procesarea la întâlnirea acestei linii
                }

                // Dacă nu suntem în secțiunea dorită, nu procesăm linia curentă
                if (!inTargetSection) continue;




                // Procesăm linia dacă suntem în secțiunea dorită
                if (trimmedLine.startsWith("SSID")) {
                    // Dacă am ajuns la un nou SSID, înseamnă că am terminat de procesat BSSID-urile anterioare
                    if (network != null) { // Dacă nu este primul SSID procesat
                        // Dacă există o rețea în curs de procesare, salvăm și resetăm
                        network.setBssids(bssidInfos);
                        networkInfos.add(network);
                        bssidInfos = new ArrayList<>();
                    }
                    network = new NetworkInfo();
                    String ssid = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                    network.setSsid(ssid);
                }    else  if (trimmedLine.startsWith("Network type            :")) {
                    String networkType = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                    network.setNetworkType(networkType);
                } else if (trimmedLine.startsWith("Authentication")) {
                    String authentication = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                    network.setAuthentication(authentication);
                } else if (trimmedLine.startsWith("Encryption")) {
                    String encryption = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                    network.setEncryption(encryption);
                }
                else if (trimmedLine.startsWith("BSSID")) {
                    // Începem un nou BSSIDInfo
                    currentBssidInfo = new NetworkInfo.BSSIDInfo();
                    String bssid = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                    currentBssidInfo.setBssid(bssid);
                    // Adăugăm BSSIDInfo curent la lista de BSSID-uri pentru acest SSID
                    bssidInfos.add(currentBssidInfo);
                } else if (currentBssidInfo != null) {
                    // Presupunem că restul informațiilor se referă la BSSID-ul curent până la întâlnirea unui nou BSSID sau SSID
                    if (trimmedLine.startsWith("Signal")) {
                        String signal = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setSignal(signal);
                    } else if (trimmedLine.startsWith("Radio type         :")) {
                        String radioType = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setRadioType(radioType);
                    } else if (trimmedLine.startsWith("Band")) {
                        String band = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setBand(band);
                    } else if (trimmedLine.startsWith("Channel            :")) {
                        String channel = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setChannel(channel);

                    } else if (trimmedLine.startsWith("Bss Load:")) {
                        isProcessingBSSID = true; // Presupunem că începem procesarea informațiilor specifice BSSID
                    } else if (isProcessingBSSID) {
                        if (trimmedLine.startsWith("Connected Stations")) {
                            String connectedStations = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                            currentBssidInfo.setConnectedStations(connectedStations);
                        } else if (trimmedLine.startsWith("Channel Utilization:")) {
                            String channelUtilization = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                            currentBssidInfo.setChannelUtilization(channelUtilization);
                        } else if (trimmedLine.startsWith("Medium Available Capacity")) {
                            String mediumAvailableCapacity = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                            currentBssidInfo.setMediumCapacity(mediumAvailableCapacity);
                            isProcessingBSSID = false; // Presupunem că după această informație, am terminat de procesat detaliile specifice BSSID
                        }
                    }

                    else if (trimmedLine.startsWith("Basic rates")) {
                        String basicRates = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setBasicrates(basicRates);
                    } else if (trimmedLine.startsWith("Other rates")) {
                        String otherRates = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setOtherrates(otherRates);
                    }

                    // else if (trimmedLine.startsWith("Radio Type")) {
                    //     String radioType = trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim();
                    //     currentBssidInfo.setRadioType(radioType);
                    // }
                }
            }
            // Asigurăm că ultima rețea este adăugată la listă, dacă există
            if (network != null) {
                network.setBssids(bssidInfos); // Salvăm ultimul set de BSSID-uri
                networkInfos.add(network); // Adăugăm ultima rețea la lista finală
            }

            return networkInfos; // Returnăm lista de rețele
        }

}
