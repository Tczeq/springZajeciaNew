package pl.szlify.coding.teacher.exception;

public class TeacherNotFoundException extends RuntimeException {

    public TeacherNotFoundException(Integer teacherId) {
        super("Teacher with ID: " + teacherId + " not found.");
    }
}
