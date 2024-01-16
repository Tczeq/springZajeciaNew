package pl.szlify.coding.lesson.model.command;

import lombok.Builder;
import lombok.Data;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateLessonCommand {

    private LocalDateTime term;
    private Teacher teacher;
    private Student student;
    private int teacherId;
    private int studentId;

    public Lesson toEntity() {
        return Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(term)
                .build();
    }
}
