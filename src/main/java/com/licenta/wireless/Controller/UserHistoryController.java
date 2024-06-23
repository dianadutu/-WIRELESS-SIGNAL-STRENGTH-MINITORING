package com.licenta.wireless.Controller;

import com.licenta.wireless.Entity.ConnectionHistoryEntity;
import com.licenta.wireless.Service.ConnectionHistoryService;
import com.licenta.wireless.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserHistoryController {

    @Autowired
    private ConnectionHistoryService connectionHistoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/userhistory")
    public String getUserHistory(HttpServletRequest request, Model model) {
        Long userId = userService.getCurrentUserId(request);
        if (userId != null) {
            List<ConnectionHistoryEntity> userHistory = connectionHistoryService.findByUserId(userId);
            model.addAttribute("userHistory", userHistory);
        }
        return "userhistory";
    }

    @DeleteMapping("/api/history/delete")
    @ResponseBody
    public ResponseEntity<String> deleteProfiles(HttpServletRequest request) {
        Long userId = userService.getCurrentUserId(request);
        if (userId != null) {
            connectionHistoryService.deleteByUserId(userId);
            return ResponseEntity.ok("{\"status\":\"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"status\":\"error\",\"message\":\"User not authenticated\"}");
        }
    }
}
