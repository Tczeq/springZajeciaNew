package pl.szlify.coding.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.command.CreateTeacherCommand;
import pl.szlify.coding.teacher.model.command.UpdateTeacherCommand;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    //  GET             - pobieranie wszystkich zasobow
    //  GET     /{id}   - pobieranie zasobuo wskazanym id
    //  POST            - utworzenie nowego zasobu
    //  PUT     /{id}   - update zasobu o wskazanym id (calosciowy)
    //  PATCH   /{id}   - update zasobu o wskazanym id (czesciowy)
    //  DELETE  /{id}   - usuwanie zasobu o wskazanym id

    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAll() {
        List<TeacherDto> teachers = teacherService.findAll();

        for (TeacherDto teacher : teachers) {
            String resourceUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/teachers/{id}")
                    .buildAndExpand(teacher.getId())
                    .toUriString();
            teacher.setUrl(resourceUrl);
        }

        return ResponseEntity.ok(teachers);
    }


//    @GetMapping("/{id}")
//    public TeacherDto getById(@PathVariable int id) {
//        return teacherService.findById(id);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getById(@PathVariable int id) {
        TeacherDto teacher = teacherService.findById(id);
        String resourceUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/teachers/{id}")
                .buildAndExpand(teacher.getId())
                .toUriString();

        teacher.setUrl(resourceUrl);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping
    public ResponseEntity<TeacherDto> create(@Valid @RequestBody CreateTeacherCommand command) {
        TeacherDto teacher = teacherService.create(command);
        String resourceUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/teachers/{id}")
                .buildAndExpand(teacher.getId())
                .toUriString();

        teacher.setUrl(resourceUrl);
        return ResponseEntity.created(URI.create(resourceUrl)).body(teacher);
    }

    @PutMapping("/{id}")
    public TeacherDto updateTeacher(@PathVariable int id, @Valid @RequestBody UpdateTeacherCommand command) {
        return teacherService.update(id, command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = "language")
    public ResponseEntity<List<TeacherDto>> getAll(@RequestParam("language") Language language) {
        List<TeacherDto> languages = teacherService.findAllByLanguage(language);


        for (TeacherDto teacher : languages) {
            String resourceUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/teachers/{id}")
                    .buildAndExpand(teacher.getId())
                    .toUriString();
            teacher.setUrl(resourceUrl);
        }


        return ResponseEntity.ok(languages);
    }

    //#####################################################################



}
