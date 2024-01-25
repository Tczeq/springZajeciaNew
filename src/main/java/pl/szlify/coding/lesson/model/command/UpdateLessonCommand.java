package pl.szlify.coding.lesson.model.command;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateLessonCommand {
    //TODO: notNull
    @FutureOrPresent(message = "Term have to be in future or present")
    private LocalDateTime term;

}
