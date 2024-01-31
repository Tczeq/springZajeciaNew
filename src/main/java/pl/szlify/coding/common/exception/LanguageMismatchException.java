package pl.szlify.coding.common.exception;

import java.text.MessageFormat;

public class LanguageMismatchException extends RuntimeException {
    public LanguageMismatchException(Integer teacherId) {
        super(MessageFormat.format("Language for teacher with id={0} not found", teacherId));
    }
}
