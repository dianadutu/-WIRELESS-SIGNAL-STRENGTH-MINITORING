package com.licenta.wireless.Controller;

import com.licenta.wireless.Entity.ConnectionHistoryEntity;
import com.licenta.wireless.Entity.UsersEntity;
import com.licenta.wireless.Repository.ConnectionHistoryRepository;
import com.licenta.wireless.Repository.UsersRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    private final UsersRepository usersRepository;
    private final ConnectionHistoryRepository connectionHistoryRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    private final Map<String, String> resetCodes = new HashMap<>();
    @Autowired
    public LoginController(UsersRepository usersRepository, ConnectionHistoryRepository connectionHistoryRepository, JavaMailSender emailSender) {
        this.usersRepository = usersRepository;
        this.connectionHistoryRepository = connectionHistoryRepository;
        this.javaMailSender = emailSender;
    }

    @GetMapping("/forgotpassword")
    public String showForgotPasswordPage() {
        return "forgotpassword"; // Acesta este numele paginii HTML fără extensie
    }

    @PostMapping("/send-reset-code")
    public ResponseEntity<?> sendResetCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Received email: " + email);  // Logare pentru verificare
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Email not found."));
        }
        // Generează un cod random din 5 cifre
        String resetCode = String.format("%05d", new SecureRandom().nextInt(100000));
        resetCodes.put(email, resetCode);
        System.out.println("Generated reset code: " + resetCode);

        // Trimite email cu codul de resetare
        try {
            sendResetCodeByEmail(email, resetCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Failed to send reset code."));
        }

        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    public void sendResetCodeByEmail(String recipientEmail, String resetCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("diana.dutuu@outlook.com");  // Use the authorized sender email
            helper.setTo(recipientEmail);
            helper.setSubject("Password Reset Code");
            helper.setText("Your reset code is: " + resetCode);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String resetCode = request.get("resetCode");
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        if (!resetCodes.containsKey(email) || !resetCodes.get(email).equals(resetCode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Invalid reset code."));
        }
        // Criptăm parola
        String encryptedPassword = encrypt(newPassword);

        // Actualizăm parola utilizatorului
        UsersEntity user = usersRepository.findByEmail(email);
        user.setPassword(encryptedPassword);
        usersRepository.save(user);

        // Ștergem codul de resetare utilizat
        resetCodes.remove(email);

        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }



    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> processRegistration(@RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith, Model model) {
        if (!password.equals(confirmPassword)) {
            if ("XMLHttpRequest".equals(requestedWith)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Passwords do not match."));
            } else {
                model.addAttribute("error", "Passwords do not match.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Passwords do not match."));
            }
        }

        // Verificăm dacă email-ul este deja folosit
        if (usersRepository.findByEmail(email) != null) {
            if ("XMLHttpRequest".equals(requestedWith)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Email already in use."));
            } else {
                model.addAttribute("error", "Email already in use.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Email already in use."));
            }
        }

        // Criptăm parola
        String encryptedPassword = encrypt(password);

        // Creăm un nou utilizator și îl salvăm în baza de date
        UsersEntity user = new UsersEntity();
        user.setEmail(email);
        user.setPassword(encryptedPassword); // Salvăm parola criptată
        usersRepository.save(user);

        if ("XMLHttpRequest".equals(requestedWith)) {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            model.addAttribute("success", "Account created successfully!");
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public void processLogin(@RequestParam String email, @RequestParam String password, HttpServletResponse response, HttpServletRequest request) throws IOException {
        UsersEntity user = usersRepository.findByEmail(email);

        if (user != null) {
            String encryptedPassword = (password);
            System.out.println("Password from DB: " + user.getPassword());
            System.out.println("Encrypted Password from input: " + encryptedPassword);

            if (user.getPassword().equals(encryptedPassword)) {

                //sa ia id ul cand ne logam
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                System.out.println("USER ID IS" + session.getAttribute("userId"));


                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true, \"message\": \"Login successful\", \"redirectUrl\": \"/home\"}");
                return;
            }
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"success\": false, \"message\": \"Invalid email or password\"}");
    }

    @GetMapping("/logout")
    public String logout() {
        // Implementarea deconectării
        return "redirect:/login";
    }

    private void saveConnectionHistory(UsersEntity user, String profileName, Integer version, String type,
                                       String controlOptions, Integer numberOfSsids, String ssidName,
                                       String networkType, String radioType, String vendorExtension,
                                       String authentication, String cipher, Boolean securityKeyPresent,
                                       String cost, Boolean congested, Boolean approachingDataLimit,
                                       Boolean overDataLimit, Boolean roaming, String costSource,
                                       String connectionMode, String networkBroadcast, String autoSwitch,
                                       String macRandomization, String eapType, String authCredential,
                                       String credentialsConfigured, String cacheUserInfo) {
        ConnectionHistoryEntity connectionHistory = new ConnectionHistoryEntity();
        connectionHistory.setUser(user);
        connectionHistory.setProfileName(profileName);
        connectionHistory.setVersion(version);
        connectionHistory.setType(type);
        connectionHistory.setControlOptions(controlOptions);
        connectionHistory.setNumberOfSsids(numberOfSsids);
        connectionHistory.setSsidName(ssidName);
        connectionHistory.setNetworkType(networkType);
        connectionHistory.setRadioType(radioType);
        connectionHistory.setVendorExtension(vendorExtension);
        connectionHistory.setAuthentication(authentication);
        connectionHistory.setCipher(cipher);
        connectionHistory.setSecurityKeyPresent(securityKeyPresent);
        connectionHistory.setCost(cost);
        connectionHistory.setCongested(congested);
        connectionHistory.setApproachingDataLimit(approachingDataLimit);
        connectionHistory.setOverDataLimit(overDataLimit);
        connectionHistory.setRoaming(roaming);
        connectionHistory.setCostSource(costSource);
        connectionHistory.setConnectionMode(connectionMode);
        connectionHistory.setNetworkBroadcast(networkBroadcast);
        connectionHistory.setAutoSwitch(autoSwitch);
        connectionHistory.setMacRandomization(macRandomization);
        connectionHistory.setEapType(eapType);
        connectionHistory.setAuthCredential(authCredential);
        connectionHistory.setCredentialsConfigured(credentialsConfigured);
        connectionHistory.setCacheUserInfo(cacheUserInfo);
        connectionHistory.setConnectionTime(LocalDateTime.now());

        connectionHistoryRepository.save(connectionHistory);
    }



    // Metoda de criptare
    public static String encrypt(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
