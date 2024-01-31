package pl.szlify.coding.lesson.exception;

public class PastTermException extends RuntimeException {
    public PastTermException() {
        super("Term cannot be from the past ");
    }
}
