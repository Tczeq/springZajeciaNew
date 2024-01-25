package pl.szlify.coding.lesson.model.command;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import lombok.Data;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateLessonCommand {
    //todo: notNull
    @FutureOrPresent(message = "Term have to be in future or present")
    private LocalDateTime term;

//    private Teacher teacher;
//
//    private Student student;

    //TODO: positive
    private int teacherId;

    //todo: positive
    private int studentId;


    public Lesson toEntity() {
        return Lesson.builder()
//                .student(student)
//                .teacher(teacher)
                .term(term)
                .deleted(false)
                .build();
    }
}
