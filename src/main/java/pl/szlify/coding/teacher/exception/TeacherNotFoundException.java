package pl.szlify.coding.teacher.exception;

import java.text.MessageFormat;

public class TeacherNotFoundException extends RuntimeException {

    public TeacherNotFoundException(Integer teacherId) {
        super(MessageFormat.format("Teacher with id={0} not found.", teacherId));
    }
}
