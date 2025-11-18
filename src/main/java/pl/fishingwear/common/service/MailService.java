package pl.fishingwear.common.service;

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

    public void sendOrderConfirmationEmail(String to, Long orderId){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Potwierdzenie zamówienia - FishingWear");
        message.setText(
                "Dziękujemy za złożenie zamówienia nr " + orderId + ".\n" +
                        "Twoje zamówienie zostało pomyślnie zarejestrowane i obecnie oczekuje na płatność. \n" +
                        "Zamówienie możesz zapłacić tutaj: http://localhost:8080/zamowienie/symulacja-platnosci?orderId=" + orderId
        );
        mailSender.send(message);

    }
}