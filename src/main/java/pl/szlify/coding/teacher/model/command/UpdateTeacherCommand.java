package pl.szlify.coding.teacher.model.command;

import lombok.Data;
import pl.szlify.coding.common.Language;

import java.util.Set;

@Data
public class UpdateTeacherCommand {

    private String firstName;
    private String lastName;
    private Set<Language> languages;
}
