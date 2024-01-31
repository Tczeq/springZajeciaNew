package pl.szlify.coding.lesson.model.command;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import pl.szlify.coding.lesson.model.Lesson;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateLessonCommand {
    //todo: notNull

    @FutureOrPresent(message = "Term have to be in future or present")
    @NotNull(message = "term is obligatory")
    private LocalDateTime term;

    @Positive(message = "id cannot be negative")
    private int teacherId;

    @Positive(message = "id cannot be negative")
    private int studentId;


    public Lesson toEntity() {
        return Lesson.builder()
                .term(term)
                .deleted(false)
                .build();
    }
}
