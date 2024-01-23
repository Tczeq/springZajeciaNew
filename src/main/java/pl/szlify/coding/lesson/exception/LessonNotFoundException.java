package pl.szlify.coding.lesson.exception;

public class LessonNotFoundException extends RuntimeException {

    public LessonNotFoundException(Integer lessonId) {
        super("Lesson with ID: " + lessonId + " not found.");
    }
}
