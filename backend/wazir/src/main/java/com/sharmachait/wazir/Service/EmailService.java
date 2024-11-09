package com.sharmachait.wazir.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendVerificationOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "Verification OTP";
        String text="Your verification code is "+otp;
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);
        mimeMessageHelper.setTo(email);
        try{
            CompletableFuture.runAsync(() -> {
                try {
                    javaMailSender.send(mimeMessage);  // Send email inside try-catch to handle exceptions
                } catch (Exception e) {
                    // Log the exception or handle it as needed
                    System.out.println("Error sending email: " + e.getMessage());
                    throw new RuntimeException(e);
                    // You can throw a custom exception or log the issue as needed
                }
            });
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println(e.getMessage());
            throw new MessagingException("OTP verification failed");
        }
    }
}

