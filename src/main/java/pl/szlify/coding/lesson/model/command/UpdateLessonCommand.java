package pl.szlify.coding.lesson.model.command;

import lombok.Data;
import pl.szlify.coding.lesson.model.Lesson;

import java.time.LocalDateTime;

@Data
public class UpdateLessonCommand {
    private LocalDateTime term;

}
