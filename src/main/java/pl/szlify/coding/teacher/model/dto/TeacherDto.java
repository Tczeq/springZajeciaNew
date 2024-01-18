package pl.szlify.coding.teacher.model.dto;

import lombok.*;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;

import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class TeacherDto {
    private int id;
    private String firstName;
    private String lastName;
    private Set<Language> languages;
    private boolean deleted;
    //    private boolean fired;
    private String url;

    public static TeacherDto fromEntity(Teacher teacher) {
        return TeacherDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .languages(teacher.getLanguages())
                .deleted(teacher.isDeleted())
//                .fired(teacher.isFired())
                .build();
    }
}
