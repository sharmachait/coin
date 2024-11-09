package com.sharmachait.wazir.Controller;

import com.sharmachait.wazir.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOtpEmail() {
        try {
            emailService.sendVerificationOtpEmail("chait8126@gmail.com", "123456");
            return ResponseEntity.ok("OTP sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP");
        }
    }
}
