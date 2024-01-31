package pl.szlify.coding.student.model.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class UpdateStudentCommand {

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The firstName must begin with a capital letter and contain from 1 to 50 letters.")
    @NotNull(message = "firstName is obligatory")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The lastname must begin with a capital letter and contain from 1 to 50 letters.")
    @NotNull(message = "lastName is obligatory")
    private String lastName;
}
