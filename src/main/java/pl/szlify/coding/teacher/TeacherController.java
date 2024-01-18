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

//    @GetMapping
//    public List<TeacherDto> getAll() {
//        return teacherService.findAll();
//    }

    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAll() {
        List<TeacherDto> all = teacherService.findAll();

        all.forEach(teacher -> {
            String resourceUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/teachers/{id}")
                    .buildAndExpand(teacher.getId())
                    .toUriString();
            teacher.setUrl(resourceUrl);
        });

        return ResponseEntity.ok(all);
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
//        return ResponseEntity.created(location).body(createdTeacher);
        return ResponseEntity.ok(teacher);
    }

//    @PostMapping
//    public TeacherDto create(@Valid @RequestBody CreateTeacherCommand command) {
//        return teacherService.create(command);
//    }
    @PostMapping
    public ResponseEntity<TeacherDto> create(@Valid @RequestBody CreateTeacherCommand command) {
        TeacherDto createdTeacher = teacherService.create(command);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(createdTeacher);
//        TeacherDto createdTeacher = teacherService.create(command);


//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(createdTeacher.getId())
//                .toUri();

        String resourceUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/teachers/{id}")
                .buildAndExpand(createdTeacher.getId())
                .toUriString();

        createdTeacher.setUrl(resourceUrl);
//        return ResponseEntity.created(location).body(createdTeacher);
        return ResponseEntity.created(URI.create(resourceUrl)).body(createdTeacher);
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

    //#####################################################################


//    @PutMapping("/fire/{id}")
//    public String firedTeacherById(@PathVariable("id") int teacherId) {
//        teacherService.fireTeacher(teacherId);
//        return "redirect:/teachers";
//    }
//
//    @PutMapping("/hire/{id}")
//    public String hiredTeacherById(@PathVariable("id") int teacherId) {
//        teacherService.hireTeacher(teacherId);
//        return "redirect:/teachers";
//    }


}
