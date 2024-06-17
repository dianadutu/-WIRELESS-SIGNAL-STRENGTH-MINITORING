/*package com.licenta.wireless.Service;





import com.licenta.wireless.Model.User;
import com.licenta.wireless.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String sendPasswordResetLink(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "Email address not found.";
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        String resetUrl = "http://localhost:8080/api/reset-password?token=" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n" + resetUrl);

        mailSender.send(mailMessage);

        return "Password reset link sent.";
    }

    public String resetPassword(String token, String password) {
        User user = userRepository.findByResetToken(token);
        if (user == null) {
            return "Invalid token.";
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        userRepository.save(user);

        return "Password successfully reset.";
    }
}
*/