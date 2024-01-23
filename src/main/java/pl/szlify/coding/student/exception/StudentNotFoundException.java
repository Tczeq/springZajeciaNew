package pl.szlify.coding.student.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Integer studentId) {
        super("Student with ID: " + studentId + " not found.");
    }
}
