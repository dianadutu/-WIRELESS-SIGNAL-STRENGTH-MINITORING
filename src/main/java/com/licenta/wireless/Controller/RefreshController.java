package com.licenta.wireless.Controller;

import com.licenta.wireless.Service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RefreshController {

    @Autowired
    private NetworkService networkService;

    @GetMapping("/refresh")
    public String showRefreshPage() {
        return "refresh"; // Returnează numele paginii HTML pe care dorești să o afișezi (fără extensie .html)
    }

    @GetMapping("/api/refresh")
    @ResponseBody
    public Map<String, Object> refreshNetworks() {
        Map<String, Object> response = new HashMap<>();
        try {
            networkService.run();
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
        }
        return response;
    }
}
