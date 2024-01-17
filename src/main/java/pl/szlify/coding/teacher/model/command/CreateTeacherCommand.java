package pl.szlify.coding.teacher.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;

import java.util.Set;

@Data
@Builder
public class CreateTeacherCommand {

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The name must begin with a capital letter and contain from 1 to 50 letters.")
    @NotBlank(message = "lastname is mandatory")
    private String firstName;
    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The lastname must begin with a capital letter and contain from 1 to 50 letters.")
    @NotBlank(message = "lastname is mandatory")
    private String lastName;
    @NotNull(message = "Languages cannot be null")
    @NotEmpty(message = "At least one language")
    private Set<Language> languages;

    public Teacher toEntity() {
        return Teacher.builder()
                .firstName(firstName)
                .lastName(lastName)
                .languages(languages)
                .build();
    }
}
