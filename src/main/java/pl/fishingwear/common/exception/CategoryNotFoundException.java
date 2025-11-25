package pl.fishingwear.common.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(){
        super("Kategoria nie zostala odnaleziona.");
    }
}
