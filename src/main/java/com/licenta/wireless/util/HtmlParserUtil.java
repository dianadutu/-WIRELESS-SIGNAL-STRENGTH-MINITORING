package com.licenta.wireless.util;

import com.licenta.wireless.Model.NetworkInfo;
import com.licenta.wireless.Model.NetworkSummary;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.apache.commons.text.StringEscapeUtils; //caract speciale

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    private int percentToDbm(int percent) {
        // Ajustăm această funcție conform nevoilor tale specifice
        return percent * (-50 + 100) / 100 - 100;
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

                //AFISARE RETELE VIZIBILE -------------------------------

                Pattern pattern = Pattern.compile("There are (\\d+) networks currently visible\\.");
                Matcher matcher = pattern.matcher(trimmedLine);

                if (matcher.find()) {
                    // Extrage numărul de rețele vizibile din text și îl convertim în int
                    int totalNetworksVisible = Integer.parseInt(matcher.group(1));

                    // Creează un obiect NetworkSummary gol
                    NetworkSummary networkSummary = new NetworkSummary();

                    // Setează numărul total de rețele vizibile
                    networkSummary.setTotalNetworksVisible(totalNetworksVisible);

                    // Presupunând că ai o listă de NetworkInfo deja populată numită networksList
                    // Setează lista de rețele în NetworkSummary
                    networkSummary.setNetworks(networkInfos);
                }

                // END RETELE VIZIBILE ---------------------------




                // Decodarea entităților HTML în linia curentă
                String decodedLine = StringEscapeUtils.unescapeHtml4(trimmedLine);

                if (decodedLine.startsWith("SSID")) {
                    // Dacă am ajuns la un nou SSID, înseamnă că am terminat de procesat BSSID-urile anterioare
                    if (network != null) { // Dacă nu este primul SSID procesat
                        // Dacă există o rețea în curs de procesare, salvăm și resetăm
                        network.setBssids(bssidInfos);
                        networkInfos.add(network);
                        bssidInfos = new ArrayList<>();
                    }
                    network = new NetworkInfo();
                    String ssid = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                    network.setSsid(ssid);
                } else if (decodedLine.startsWith("Network type            :")) {
                    String networkType = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                    network.setNetworkType(networkType);
                } else if (decodedLine.startsWith("Authentication")) {
                    String authentication = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                    network.setAuthentication(authentication);
                } else if (decodedLine.startsWith("Encryption")) {
                    String encryption = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                    network.setEncryption(encryption);
                }
                else if (decodedLine.startsWith("BSSID")) {
                    // Începem un nou BSSIDInfo
                    currentBssidInfo = new NetworkInfo.BSSIDInfo();
                    String bssid = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                    currentBssidInfo.setBssid(bssid);
                    // Adăugăm BSSIDInfo curent la lista de BSSID-uri pentru acest SSID
                    bssidInfos.add(currentBssidInfo);
                } else if (currentBssidInfo != null) {
                    // Presupunem că restul informațiilor se referă la BSSID-ul curent până la întâlnirea unui nou BSSID sau SSID
                    if (trimmedLine.startsWith("Signal")) {
                        String signal = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                        // Presupunând că signalStr este un procent sub formă de String, ex: "70%"
                        signal = signal.replace("%", ""); // Eliminăm simbolul procent
                        try {
                            int signalPercent = Integer.parseInt(signal); // Convertim în int
                            int signalDbm = percentToDbm(signalPercent); // Aplicăm conversia
                            // Aici setezi valoarea în dBm în loc de procent
                            currentBssidInfo.setSignal(String.valueOf(signalDbm) + " dBm"); // Presupunem că setter-ul acceptă un String
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            // Gestionăm cazul în care conversia din String în int eșuează
                        }
                    } else if (decodedLine.startsWith("Radio type         :")) {
                        String radioType = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setRadioType(radioType);
                    } else if (decodedLine.startsWith("Band")) {
                        String band = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setBand(band);
                    } else if (decodedLine.startsWith("Channel            :")) {
                        String channel = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setChannel(channel);
                    } else if (decodedLine.startsWith("Bss Load:")) {
                        isProcessingBSSID = true;
                    } else if (isProcessingBSSID) {
                        if (decodedLine.startsWith("Connected Stations")) {
                            String connectedStations = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                            currentBssidInfo.setConnectedStations(connectedStations);
                        } else if (decodedLine.startsWith("Channel Utilization:")) {
                            String channelUtilization = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                            currentBssidInfo.setChannelUtilization(channelUtilization);
                        } else if (decodedLine.startsWith("Medium Available Capacity")) {
                            String mediumAvailableCapacity = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                            currentBssidInfo.setMediumCapacity(mediumAvailableCapacity);
                            isProcessingBSSID = false;
                        }
                    } else if (decodedLine.startsWith("Basic rates")) {
                        String basicRates = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
                        currentBssidInfo.setBasicrates(basicRates);
                    } else if (decodedLine.startsWith("Other rates")) {
                        String otherRates = decodedLine.substring(decodedLine.indexOf(":") + 1).trim();
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
