package pl.fishingwear.product.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException() {
        super("Produkt nie istnieje.");
    }
}
