package com.newsgroupmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    public void sendSignupOtpEmail(String toEmail, String otp){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject("Verify your email to complete your News Group Management registration");
            StringBuilder mailContent = new StringBuilder();
            mailContent.append("Hi there,\n\n");
            mailContent.append("Thank you for signing up with News Group Management — your secure platform for trusted and multilingual news sharing.\n\n");
            mailContent.append("To complete your registration, please verify your email address by entering the following One-Time Password (OTP) on the website:\n\n");
            mailContent.append("Your OTP: ").append(otp).append("\n\n");
            mailContent.append("This OTP is valid for the next 10 minutes. Please do not share it with anyone.\n\n");
            mailContent.append("If you did not request this registration, you can safely ignore this email.\n\n");
            mailContent.append("Thanks,\n");
            mailContent.append("The News Group Management Team\n");
            mailContent.append("https://www.newsgroupmanagement.com");
            message.setText(mailContent.toString());
            mailSender.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendLocalSignupWelcomeEmail(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject("Welcome to News Group Management – Your Account Has Been Created");

            StringBuilder mailContent = new StringBuilder();
            mailContent.append("Hi there,\n\n");
            mailContent.append("Thank you for signing up with News Group Management — your secure platform for trusted and multilingual news sharing.\n\n");
            mailContent.append("We're excited to have you on board! Your account has been successfully created:\n\n");
            mailContent.append("Email: ").append(toEmail).append("\n\n");
            mailContent.append("You can now log in using your email and password to share and explore verified news across languages and communities.\n\n");
            mailContent.append("If you did not sign up for this account, please contact our support team or secure your account immediately.\n\n");
            mailContent.append("Thanks,\n");
            mailContent.append("The News Group Management Team\n");
            mailContent.append("https://www.newsgroupmanagement.com");

            message.setText(mailContent.toString());
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendGoogleSignupWelcomeEmail(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject("Welcome to News Group Management – Your Account Has Been Created");

            StringBuilder mailContent = new StringBuilder();
            mailContent.append("Hi there,\n\n");
            mailContent.append("Welcome to News Group Management — your secure platform for trusted and multilingual news sharing.\n\n");
            mailContent.append("We're happy to let you know that your account has been successfully created using your Google account:\n\n");
            mailContent.append("Email: ").append(toEmail).append("\n\n");
            mailContent.append("You can now log in anytime using your Google credentials to share and explore verified news across languages and communities.\n\n");
            mailContent.append("If you did not sign up for this account, please contact our support team or secure your Google account immediately.\n\n");
            mailContent.append("Thanks,\n");
            mailContent.append("The News Group Management Team\n");
            mailContent.append("https://www.newsgroupmanagement.com");

            message.setText(mailContent.toString());
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTwoFactorOtpEmail(String toEmail, String otp){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject("Your Two-Factor Authentication (2FA) Code - News Group Management");

            StringBuilder mailContent = new StringBuilder();
            mailContent.append("Hi there,\n\n");
            mailContent.append("To complete your login to News Group Management, please verify your identity by entering the following One-Time Password (OTP):\n\n");
            mailContent.append("Your OTP: ").append(otp).append("\n\n");
            mailContent.append("This OTP is valid for the next 10 minutes and is required to complete your two-factor authentication.\n");
            mailContent.append("Please do not share this code with anyone.\n\n");
            mailContent.append("If you did not try to log in, please secure your account immediately.\n\n");
            mailContent.append("Thanks,\n");
            mailContent.append("The News Group Management Team\n");
            mailContent.append("https://www.newsgroupmanagement.com");

            message.setText(mailContent.toString());
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendForgotPasswordOtpEmail(String toEmail, String otp){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject("Reset Your Password - News Group Management");

            StringBuilder mailContent = new StringBuilder();
            mailContent.append("Hi there,\n\n");
            mailContent.append("We received a request to reset the password for your News Group Management account.\n\n");
            mailContent.append("To proceed, please enter the following One-Time Password (OTP) on the website:\n\n");
            mailContent.append("Your OTP: ").append(otp).append("\n\n");
            mailContent.append("This OTP is valid for the next 10 minutes. Please do not share it with anyone.\n\n");
            mailContent.append("If you did not request a password reset, you can safely ignore this email and your password will remain unchanged.\n\n");
            mailContent.append("Thanks,\n");
            mailContent.append("The News Group Management Team\n");
            mailContent.append("https://www.newsgroupmanagement.com");

            message.setText(mailContent.toString());
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNewsNotificationEmail(String toEmail, String postedBy, String categoryName, String newsTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject("📰 New News Alert – Stay Updated with the Latest Post");

            StringBuilder mailContent = new StringBuilder();
            mailContent.append("Hi there,\n\n");
            mailContent.append("A new news article has just been published on News Group Management!\n\n");
            mailContent.append("📝 Title: ").append(newsTitle).append("\n");
            mailContent.append("📂 Category: ").append(categoryName).append("\n");
            mailContent.append("👤 Posted by: ").append(postedBy).append("\n\n");
            mailContent.append("Visit the platform now to read the latest updates, share your thoughts, and stay informed.\n\n");
            mailContent.append("If you no longer wish to receive news alerts, you can unsubscribe from your profile settings.\n\n");
            mailContent.append("Thanks,\n");
            mailContent.append("The News Group Management Team\n");
            mailContent.append("https://www.newsgroupmanagement.com");

            message.setText(mailContent.toString());
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
