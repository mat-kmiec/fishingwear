package pl.fishingwear.blog.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("Post not found");
    }
}
