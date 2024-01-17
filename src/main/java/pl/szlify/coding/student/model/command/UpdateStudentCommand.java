package pl.szlify.coding.student.model.command;

import lombok.Data;


@Data
public class UpdateStudentCommand {

    private String firstName;
    private String lastName;
}
