package pl.szlify.coding.student.model.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.student.model.Student;

@Getter
@Builder
@EqualsAndHashCode
public class StudentDto {
    private int id;
    private String firstName;
    private String lastName;
    private Language language;
    private Integer teacherId;
    private boolean deleted;

    public static StudentDto fromEntity(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .language(student.getLanguage())
                .teacherId(student.getTeacher() != null ? student.getTeacher().getId() : null)
                .deleted(student.isDeleted())
                .build();
    }
}