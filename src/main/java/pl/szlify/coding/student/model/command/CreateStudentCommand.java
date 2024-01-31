package pl.szlify.coding.student.model.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.student.model.Student;

@Data
@Builder
public class CreateStudentCommand {

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The name must begin with a capital letter and contain from 1 to 50 letters.")
    @NotNull(message = "firstname is obligatory")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The lastname must begin with a capital letter and contain from 1 to 50 letters.")
    @NotNull(message = "lastname is obligatory")
    private String lastName;

    @NotNull(message = "At least one language")
    private Language language;

    @Positive(message = "id cannot be negative")
    private int teacherId;

    public Student toEntity() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .language(language)
                .build();
    }
}
