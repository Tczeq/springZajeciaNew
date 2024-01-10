package pl.szlify.coding.lesson.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Getter
@Builder
public class LessonDto {

    private int id;
    private LocalDateTime term;
    private Teacher teacher;
    private Student student;

    public static LessonDto fromEntity(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .term(lesson.getTerm())
                .teacher(lesson.getTeacher())
                .student(lesson.getStudent())
                .build();
    }
}
