package pl.fishingwear.theme.exception;

public class ThemeNotFoundException extends RuntimeException {
    public ThemeNotFoundException(){
        super("Nie znaleziono takiego motywu.");
    }
}
