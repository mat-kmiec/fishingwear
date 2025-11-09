package pl.fishingwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Resetowanie hasła - FishingWear");
        message.setText("Aby zresetować hasło, kliknij w link:\n" + link + "\n\nLink ważny przez 1 godzinę.");
        mailSender.send(message);
    }
}