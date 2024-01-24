package pl.szlify.coding.lesson.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.szlify.coding.lesson.model.Lesson;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
public class LessonDto {

    private int id;
    private LocalDateTime term;
    private Integer teacherId;
    private Integer studentId;
    private boolean deleted;

    public static LessonDto fromEntity(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .term(lesson.getTerm())
                .teacherId(lesson.getTeacher().getId())
                .studentId(lesson.getStudent().getId())
                .deleted(lesson.isDeleted())
                .build();
    }
}
