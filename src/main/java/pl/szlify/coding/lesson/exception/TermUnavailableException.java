package pl.szlify.coding.lesson.exception;

public class TermUnavailableException extends RuntimeException {
    public TermUnavailableException() {
        super("Term unavailable");
    }
}
