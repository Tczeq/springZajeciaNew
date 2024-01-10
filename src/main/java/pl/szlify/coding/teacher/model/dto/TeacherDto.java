package pl.szlify.coding.teacher.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;

import java.util.Set;

@Getter
@Builder
@EqualsAndHashCode
public class TeacherDto {
    private int id;
    private String firstName;
    private String lastName;
    private Set<Language> languages;
    private boolean deleted;
    private boolean fired;

    public static TeacherDto fromEntity(Teacher teacher) {
        return TeacherDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .languages(teacher.getLanguages())
                .deleted(teacher.isDeleted())
                .fired(teacher.isFired())
                .build();
    }
}
