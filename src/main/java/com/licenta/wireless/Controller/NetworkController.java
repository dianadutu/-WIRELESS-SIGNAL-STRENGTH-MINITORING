package com.licenta.wireless.Controller;

import com.licenta.wireless.Model.NetworkInfo;
import com.licenta.wireless.Model.NetworkSummary;
import com.licenta.wireless.util.HtmlParserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
public class NetworkController {

    private final HtmlParserUtil htmlParserUtil;

    public NetworkController(HtmlParserUtil htmlParserUtil) {
        this.htmlParserUtil = htmlParserUtil;
    }

    // JSON-ul
    @GetMapping("/api/networks")
    @ResponseBody
    public List<NetworkInfo> getNetworkInfos() {
        return htmlParserUtil.parseHtml();
    }

    // Servește pagina HTML home.html
    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<NetworkInfo> networkInfos = htmlParserUtil.parseHtml();
        int totalNetworksVisible = networkInfos.size();
        NetworkSummary networkSummary = new NetworkSummary(totalNetworksVisible, networkInfos);

        model.addAttribute("networkSummary", networkSummary);
        model.addAttribute("networks", networkInfos);

        return "home";
    }

    @GetMapping("/network/{index}")
    public String showNetworkPage(Model model, @PathVariable("index") int index) {
        List<NetworkInfo> networkInfos = htmlParserUtil.parseHtml();

        if (index >= 0 && index < networkInfos.size()) {
            NetworkInfo networkInfo = networkInfos.get(index);
            List<NetworkInfo> singleNetworkInfo = new ArrayList<>();
            singleNetworkInfo.add(networkInfo);
            model.addAttribute("networks", singleNetworkInfo);
            return "network";
        } else {
            // Dacă index-ul este invalid
            return "error";
        }
    }

    // Servește pagina HTML networks.html
    @GetMapping("/networks")
    public String showNetworksPage(Model model) {
        List<NetworkInfo> networkInfos = htmlParserUtil.parseHtml();
        int totalNetworksVisible = networkInfos.size();
        NetworkSummary networkSummary = new NetworkSummary(totalNetworksVisible, networkInfos);

        model.addAttribute("networkSummary", networkSummary);
        model.addAttribute("networks", networkInfos);

        return "networks";
    }

    // Funcția personalizată pentru extragerea valorii semnalului
    public static int extractSignalStrength(String signal) {
        try {
            return Integer.parseInt(signal.split(" ")[0]);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE; // sau orice valoare implicită
        }
    }
    // Funcția pentru a determina clasa CSS pe baza valorii semnalului
    public String getSignalStrengthCssClass(String signal) {
        int signalStrength = extractSignalStrength(signal);
        if (signalStrength >= -60 && signalStrength <= -30) {
            return "signal-strong";
        } else if (signalStrength >= -79 && signalStrength <= -59) {
            return "signal-medium";
        } else if (signalStrength >= -92 && signalStrength <= -80) {
            return "signal-weak";
        } else {
            return "";
        }
    }
}