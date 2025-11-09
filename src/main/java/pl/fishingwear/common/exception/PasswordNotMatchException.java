package pl.fishingwear.common.exception;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException() {
        super("Hasła nie pasują do siebie.");
    }
}
