package com.licenta.wireless.Controller;

import com.licenta.wireless.Model.NetworkInfo;
import com.licenta.wireless.util.HtmlParserUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class NetworkController {

    private final HtmlParserUtil htmlParserUtil;

    // Injectează HtmlParserUtil folosind constructorul
    public NetworkController(HtmlParserUtil htmlParserUtil) {
        this.htmlParserUtil = htmlParserUtil;
    }

    @GetMapping("/networks")
    public List<NetworkInfo> getNetworkInfos() {
        // Presupunem că metoda parseHtml este modificată pentru a fi accesibilă public sau printr-un serviciu
        return htmlParserUtil.parseHtml();
    }
}