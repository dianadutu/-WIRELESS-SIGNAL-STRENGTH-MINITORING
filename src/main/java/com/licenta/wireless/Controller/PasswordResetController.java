/*package com.licenta.wireless.Controller;

import com.licenta.wireless.Service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email) {
        return passwordResetService.sendPasswordResetLink(email);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        return passwordResetService.resetPassword(token, password);
    }
}
*/