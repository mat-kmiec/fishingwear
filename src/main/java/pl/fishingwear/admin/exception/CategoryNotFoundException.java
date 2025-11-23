package pl.fishingwear.admin.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(){
        super("Kategoria nie zostala odnaleziona.");
    }
}
