package pl.fishingwear.common.exception;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String message) {
        super("Ten adres e-mail jest już zajęty:");
    }
}
