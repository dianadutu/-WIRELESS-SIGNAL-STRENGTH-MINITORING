package com.licenta.wireless.Controller;

import com.licenta.wireless.Model.NetworkInfo;
import com.licenta.wireless.util.HtmlParserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@Controller
public class NetworkController {

    private final HtmlParserUtil htmlParserUtil;

    public NetworkController(HtmlParserUtil htmlParserUtil) {
        this.htmlParserUtil = htmlParserUtil;
    }

    // Servește JSON-ul
    @GetMapping("/networks")
    @ResponseBody
    public List<NetworkInfo> getNetworkInfos() {
        return htmlParserUtil.parseHtml();
    }

    // Servește pagina HTML
    @GetMapping("/network-page")
    public String showNetworksPage(Model model) {
        List<NetworkInfo> networkInfos = htmlParserUtil.parseHtml();
        model.addAttribute("networks", networkInfos);
        return "networks"; // Numele template-ului Thymeleaf
    }
}
