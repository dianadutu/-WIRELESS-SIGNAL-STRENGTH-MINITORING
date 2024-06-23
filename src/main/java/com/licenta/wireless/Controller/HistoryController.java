/*historycontroller*/

package com.licenta.wireless.Controller;

import com.licenta.wireless.Entity.ConnectionHistoryEntity;
import com.licenta.wireless.Entity.UsersEntity;
import com.licenta.wireless.Model.HistoryModel;
import com.licenta.wireless.Service.ConnectionHistoryService;
import com.licenta.wireless.Service.UserService;
import com.licenta.wireless.util.HistoryUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private HistoryUtil historyUtil;

    private final ConnectionHistoryService connectionHistoryService;
    private final UserService userService;

    public HistoryController(ConnectionHistoryService connectionHistoryService,  UserService userService) {
        this.connectionHistoryService = connectionHistoryService;
        this.userService = userService;
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<HistoryModel> userProfiles = historyUtil.parseHtml();
        model.addAttribute("userProfiles", userProfiles);
        return "history";
    }


    @GetMapping("/api/history")
    public List<HistoryModel> getHistory() {
        return historyUtil.parseHtml();
    }

    @PostMapping("/history/add/")
    @ResponseBody
    public ResponseEntity<String> saveHistory(@RequestBody HistoryModel historyModel, HttpServletRequest request) {
        ConnectionHistoryEntity connectionHistoryEntity = new ConnectionHistoryEntity();
        Long userId = userService.getCurrentUserId(request);
        UsersEntity user = userService.findById(userId);
        connectionHistoryEntity.setUser(user);

        System.out.println(historyModel);

        connectionHistoryEntity.setApproachingDataLimit(historyModel.isApproachingDataLimit());
        connectionHistoryEntity.setAuthCredential(historyModel.getAuthCredential());
        connectionHistoryEntity.setAuthentication(historyModel.getAuthentication());
        connectionHistoryEntity.setAutoSwitch(String.valueOf(historyModel.getAutoSwitch()));
        connectionHistoryEntity.setCacheUserInfo(historyModel.getCacheUserInfo());
        connectionHistoryEntity.setCipher(historyModel.getCipher());
        connectionHistoryEntity.setCongested(historyModel.isCongested());
        connectionHistoryEntity.setConnectionMode(historyModel.getConnectionMode());
        connectionHistoryEntity.setControlOptions(historyModel.getControlOptions());
        connectionHistoryEntity.setCost(historyModel.getCost());
        connectionHistoryEntity.setCostSource(historyModel.getCostSource());
        connectionHistoryEntity.setCredentialsConfigured(String.valueOf(historyModel.getCredentialsConfigured()));
        connectionHistoryEntity.setEapType(historyModel.getEapType());
        connectionHistoryEntity.setMacRandomization(historyModel.getMacRandomization());
        connectionHistoryEntity.setNetworkBroadcast(historyModel.getNetworkBroadcast());
        connectionHistoryEntity.setNetworkType(historyModel.getNetworkType());
        connectionHistoryEntity.setNumberOfSsids(historyModel.getNumberOfSSIDs());
        connectionHistoryEntity.setOverDataLimit(historyModel.isOverDataLimit());
        connectionHistoryEntity.setProfileName(historyModel.getProfileName());
        connectionHistoryEntity.setRadioType(historyModel.getRadioType());
        connectionHistoryEntity.setRoaming(historyModel.isRoaming());
        connectionHistoryEntity.setSecurityKeyPresent(historyModel.isSecurityKeyPresent());
        connectionHistoryEntity.setSsidName(historyModel.getSsidName());
        connectionHistoryEntity.setType(historyModel.getType());
        connectionHistoryEntity.setVendorExtension(historyModel.getVendorExtension());
        connectionHistoryEntity.setVersion(historyModel.getVersion());

        connectionHistoryService.saveHistory(connectionHistoryEntity);

        return ResponseEntity.ok("{\"status\":\"success\"}");
    }
}
