package pl.fishingwear.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CheckoutFormDto(
        @NotEmpty(message = "Imię jest wymagane")
        @Size(min = 2, message = "Imię musi mieć co najmniej 2 znaki")
        String firstName,

        @NotEmpty(message = "Nazwisko jest wymagane")
        @Size(min = 2, message = "Nazwisko musi mieć co najmniej 2 znaki")
        String lastName,

        @NotEmpty(message = "E-mail jest wymagany")
        @Email(message = "Niepoprawny format adresu e-mail")
        String email,

        @NotEmpty(message = "Telefon jest wymagany")
        @Pattern(regexp = "^(\\+48)?[0-9]{9}$", message = "Niepoprawny format numeru telefonu (oczekiwano 9 cyfr, opcjonalnie +48)")
        String phone,

        @NotEmpty(message = "Adres jest wymagany")
        String shippingAddress,

        @NotEmpty(message = "Kod pocztowy jest wymagany")
        @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Niepoprawny format kodu pocztowego (oczekiwano XX-XXX)")
        String zipCode,

        @NotEmpty(message = "Miasto jest wymagane")
        String city,

        @NotEmpty(message = "Metoda dostawy jest wymagana")
        String shippingMethod,

        @NotEmpty(message = "Metoda płatności jest wymagana")
        String paymentMethod
) {}
