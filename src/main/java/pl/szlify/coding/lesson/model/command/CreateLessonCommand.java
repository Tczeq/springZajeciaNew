package pl.szlify.coding.lesson.model.command;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateLessonCommand {

    @FutureOrPresent(message = "Term have to be in future or present")
    private LocalDateTime term;
//    @NotNull(message = "teacher is valid")
    //TODO;
    private Teacher teacher;
//    @NotNull(message = "student is valid")
    private Student student;
    @Positive(message = "Teacher id have to be positive")
    private int teacherId;
    @Positive(message = "Student id have to be positive")
    private int studentId;

    public Lesson toEntity() {
        return Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(term)
                .build();
    }
}
