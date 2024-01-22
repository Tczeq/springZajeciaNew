package pl.szlify.coding.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.command.CreateTeacherCommand;
import pl.szlify.coding.teacher.model.command.UpdateTeacherCommand;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    //TODO;

    //  GET             - pobieranie wszystkich zasobow
    //  GET     /{id}   - pobieranie zasobuo wskazanym id
    //  POST            - utworzenie nowego zasobu
    //  PUT     /{id}   - update zasobu o wskazanym id (calosciowy)
    //  PATCH   /{id}   - update zasobu o wskazanym id (czesciowy)
    //  DELETE  /{id}   - usuwanie zasobu o wskazanym id

    @GetMapping
    public List<TeacherDto> getAll() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    public TeacherDto getById(@PathVariable int id) {
        return teacherService.findById(id);
    }

    @PostMapping
    public TeacherDto create(@Valid @RequestBody CreateTeacherCommand command) {
        return teacherService.create(command);
    }

    @PutMapping("/{id}")
    public TeacherDto updateTeacher(@PathVariable int id, @Valid @RequestBody UpdateTeacherCommand command) {
        return teacherService.update(id, command);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        teacherService.deleteById(id);
    }

    @GetMapping(params = "language")
    public List<TeacherDto> getAll(@RequestParam("language") Language language) {
        return teacherService.findAllByLanguage(language);
    }
}
