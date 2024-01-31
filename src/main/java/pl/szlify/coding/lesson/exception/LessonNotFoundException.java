package pl.szlify.coding.lesson.exception;

import java.text.MessageFormat;

public class LessonNotFoundException extends RuntimeException {

    public LessonNotFoundException(Integer lessonId) {
        super(MessageFormat.format("Lesson with id={0} not found.", lessonId));
    }
}
