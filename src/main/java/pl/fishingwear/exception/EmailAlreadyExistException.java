package pl.fishingwear.exception;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String message) {
        super("Email " + message + " already exist");
    }
}
