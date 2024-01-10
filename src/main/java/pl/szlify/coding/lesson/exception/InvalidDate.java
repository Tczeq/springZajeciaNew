package pl.szlify.coding.lesson.exception;

public class InvalidDate extends RuntimeException {
    public InvalidDate(String message) {
        super(message);
    }
}
