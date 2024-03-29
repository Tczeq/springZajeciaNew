package pl.szlify.coding.student;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szlify.coding.student.model.command.CreateStudentCommand;
import pl.szlify.coding.student.model.command.UpdateStudentCommand;
import pl.szlify.coding.student.model.dto.StudentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> getAll() {
        return studentService.findAll();
    }

    @PostMapping
    public StudentDto create(@Valid @RequestBody CreateStudentCommand command) {
        return studentService.create(command, command.getTeacherId());
    }

    @GetMapping(params = "teacher")
    public List<StudentDto> getStudentsByTeacher(@RequestParam("teacher") int teacherId) {
        return studentService.findStudentsByTeacher(teacherId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        studentService.deleteById(id);
    }

    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable int id, @Valid @RequestBody UpdateStudentCommand command) {
        return studentService.update(id, command);
    }

    //#####################################################################


}
