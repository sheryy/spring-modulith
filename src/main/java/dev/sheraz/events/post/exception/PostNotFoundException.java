package dev.sheraz.events.post.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Integer id) {
        super("Post %d not found".formatted(id));
    }
}
