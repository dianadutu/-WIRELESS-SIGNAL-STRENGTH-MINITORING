package com.licenta.wireless.util;

import com.licenta.wireless.Model.HistoryModel;
import org.apache.commons.text.StringEscapeUtils;
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
@Order(3) // Rulează după HtmlParserUtil
public class HistoryUtil implements CommandLineRunner, Ordered {

    @Value("${network.filePath}")
    private String filePath;

    @Override
    public void run(String... args) throws Exception {
        parseHtml();
    }

    @Override
    public int getOrder() {
        return 3;
    }

    public List<HistoryModel> parseHtml() {
        List<HistoryModel> userProfiles = new ArrayList<>();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Document doc = Jsoup.parse(content);

            Elements textAreas = doc.select("textarea.command-output");
            for (Element textArea : textAreas) {
                String textAreaContent = textArea.text();
                List<HistoryModel> extractedProfiles = extractUserProfiles(textAreaContent);
                userProfiles.addAll(extractedProfiles);

                // Adaugă detaliile pentru fiecare profil
                for (HistoryModel profile : extractedProfiles) {
                    extractHistoryDetails(profile, textAreaContent, profile.getProfileName());
                }

            }

            System.out.println("Procesare fișier HTML completată.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userProfiles;
    }

    private List<HistoryModel> extractUserProfiles(String content) {
        List<HistoryModel> userProfiles = new ArrayList<>();
        boolean inProfilesSection = false;

        for (String line : content.split("\n")) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("============================= SHOW PROFILES ===========================")) {
                inProfilesSection = true;
                continue;
            }

            if (inProfilesSection && trimmedLine.startsWith("========================== SHOW PROFILES NAMES ========================")) {
                break;
            }

            if (inProfilesSection && trimmedLine.startsWith("All User Profile")) {
                String profileName = trimmedLine.split(":")[1].trim();
                userProfiles.add(new HistoryModel(profileName));
            }
        }

        return userProfiles;
    }

    private void extractHistoryDetails(HistoryModel profile, String content, String profileName) {
        String[] lines = content.split("\\r?\\n");
        boolean isProfileSection = false;
        boolean isControlOptionsSection = false;
        boolean is8021xSection = false; // Adăugăm o nouă variabilă pentru secțiunea "802.1X"

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Decodarea entităților HTML în linia curentă
            String decodedLine = StringEscapeUtils.unescapeHtml4(trimmedLine);

            if (decodedLine.startsWith("Profile " + profileName)) {
                isProfileSection = true;
                continue;
            }

            if (isProfileSection && decodedLine.startsWith("Cost settings")) {
                break;
            }

            if (isProfileSection && decodedLine.startsWith("Control options")) {
                isControlOptionsSection = true;
                continue;
            }

            if (isProfileSection && decodedLine.isEmpty()) {
                isControlOptionsSection = false;
            }

            if (isProfileSection && decodedLine.startsWith("802.1X")) {
                // Dacă se găsește detaliul "802.1X", începe să proceseze următoarele linii
                is8021xSection = true; // Setăm variabila pentru secțiunea "802.1X" la true
                continue;
            }

            if (is8021xSection) {
                String[] parts = decodedLine.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = decodeAndCleanText(parts[1].trim());

                    // Verificăm dacă detaliile pentru "802.1X" sunt disponibile și le adăugăm la profil doar dacă sunt
                    if (!value.isEmpty()) {
                        switch (key) {
                            case "EAP type":
                                profile.setEapType(value);
                                break;
                            case "802.1X auth credential":
                                profile.setAuthCredential(value);
                                break;
                            case "Credentials configured":
                                profile.setCredentialsConfigured(value);
                                break;
                            case "Cache user information":
                                profile.setCacheUserInfo(value);
                                break;
                        }
                    }
                }
            }



            if (isProfileSection) {
                String[] parts = decodedLine.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = decodeAndCleanText(parts[1].trim());

                    switch (key) {
                        case "Version":
                            profile.setVersion(Integer.parseInt(value));
                            break;
                        case "Type":
                            profile.setType(value);
                            break;
                        case "Control options":
                            profile.setControlOptions(value);
                            break;
                        case "Number of SSIDs":
                            profile.setNumberOfSSIDs(Integer.parseInt(value));
                            break;
                        case "SSID name":
                            profile.setSsidName(value);
                            break;
                        case "Network type":
                            profile.setNetworkType(value);
                            break;
                        case "Radio type":
                            profile.setRadioType(value);
                            break;
                        case "Vendor extension":
                            profile.setVendorExtension(value);
                            break;
                        case "Authentication":
                            profile.setAuthentication(value);
                            break;
                        case "Cipher":
                            profile.setCipher(value);
                            break;
                        case "Security key":
                            profile.setSecurityKeyPresent("Present".equals(value));
                            break;
                        case "Cost":
                            profile.setCost(value);
                            break;
                        case "Congested":
                            profile.setCongested("Yes".equals(value));
                            break;
                        case "Approaching Data Limit":
                            profile.setApproachingDataLimit("Yes".equals(value));
                            break;
                        case "Over Data Limit":
                            profile.setOverDataLimit("Yes".equals(value));
                            break;
                        case "Roaming":
                            profile.setRoaming("Yes".equals(value));
                            break;
                        case "Cost Source":
                            profile.setCostSource(value);
                            break;
                        case "Connection mode":
                            profile.setConnectionMode(value);
                            break;
                        case "Network broadcast":
                            profile.setNetworkBroadcast(value);
                            break;
                        case "AutoSwitch":
                            profile.setAutoSwitch(value);
                            break;
                        case "MAC Randomization":
                            profile.setMACRandomization(value);
                            break;
                    }
                }
            }
        }
    }

    private String decodeAndCleanText(String text) {
        // Decodăm entitățile HTML și eliminăm caracterele nerecunoscute
        String decodedText = StringEscapeUtils.unescapeHtml4(text);
        return decodedText.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "");
    }
}
