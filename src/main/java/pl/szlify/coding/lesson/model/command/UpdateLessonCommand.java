package pl.szlify.coding.lesson.model.command;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateLessonCommand {

    @FutureOrPresent(message = "Term have to be in future or present")
    @NotNull(message = "term is obligatory")
    private LocalDateTime term;

}
