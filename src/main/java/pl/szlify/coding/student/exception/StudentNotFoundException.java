package pl.szlify.coding.student.exception;

import java.text.MessageFormat;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Integer studentId) {
        super(MessageFormat.format("Student with id={0} not found.", studentId));
    }
}
