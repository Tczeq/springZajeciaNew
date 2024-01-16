package pl.szlify.coding.lesson.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
public class LessonDto {

    private int id;
    private LocalDateTime term;
//    private Teacher teacher;
//    private Student student;

    private Integer teacherId;
    private Integer studentId;
    private Boolean deleted;

    public static LessonDto fromEntity(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .term(lesson.getTerm())
                .teacherId(lesson.getTeacher() != null ? lesson.getTeacher().getId() : null)
                .studentId(lesson.getStudent() != null ? lesson.getStudent().getId() : null)
                .deleted(lesson.getIsDeleted() != null ? lesson.getIsDeleted() : null)
                .build();
    }
}
