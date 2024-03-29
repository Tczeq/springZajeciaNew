package pl.szlify.coding.teacher.model.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import pl.szlify.coding.common.Language;

import java.util.Set;

@Data
@Builder
public class UpdateTeacherCommand {

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The name must begin with a capital letter and contain from 1 to 50 letters.")
    @NotNull(message = "firstName is obligatory")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The lastname must begin with a capital letter and contain from 1 to 50 letters.")
    @NotNull(message = "lastName is obligatory")
    private String lastName;

    @NotEmpty(message = "At least one language")
    private Set<Language> languages;
}
