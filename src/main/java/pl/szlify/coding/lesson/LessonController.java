package pl.szlify.coding.lesson;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szlify.coding.lesson.model.command.CreateLessonCommand;
import pl.szlify.coding.lesson.model.command.UpdateLessonCommand;
import pl.szlify.coding.lesson.model.dto.LessonDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;


    @GetMapping
    public List<LessonDto> getAll() {
        return lessonService.findAll();
    }


    /*
     * Dodaje tutaj Model, aby mozna bylo po froncie wyswietlac wiadomosci
     * */
//    @PostMapping("/create")
//    public String create(Lesson lesson, @RequestParam int teacherId, @RequestParam int studentId, Model model) {
//        try {
//            lessonService.create(lesson, teacherId, studentId);
//            return "redirect:/lessons";
//        } catch (InvalidDate e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "lesson/form";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "redirect:/lessons";
//        }
//
//    }

    @PostMapping
    public LessonDto create(@RequestBody CreateLessonCommand command) {
        return lessonService.create(command, command.getTeacherId(), command.getStudentId());
    }

    @PutMapping("/{id}")
    public LessonDto updateLesson(@PathVariable int id, @RequestBody UpdateLessonCommand command) {
        return lessonService.update(id, command);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        lessonService.deleteById(id);
    }


    //#####################################################################


}
