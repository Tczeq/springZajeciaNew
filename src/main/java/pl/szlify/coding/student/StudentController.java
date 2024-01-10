package pl.szlify.coding.student;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.student.model.dto.StudentDto;
import pl.szlify.coding.teacher.TeacherService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("students", studentService.findAll());
        return "student/list";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model){
        model.addAttribute("languages", Language.values());
        model.addAttribute("teachers", teacherService.findAll());
        return "student/form";
    }

    @PostMapping("/create")
    public String create(Student student, @RequestParam int teacherId){
        studentService.create(student, teacherId);
        return "redirect:/students";
    }

    @GetMapping(params = "teacher")
    @ResponseBody
    public List<StudentDto> getStudentsByTeacher(@RequestParam("teacher") int teacherId) {
//        Teacher teacher = teacherService.findTeacherById(teacherId);
//        return studentService.findStudentsByTeacher(teacher).stream()
//                .map(StudentDto::fromEntity)
//                .toList();
        return studentService.findStudentsByTeacher(teacherId);
    }


    @DeleteMapping
    @ResponseBody
    public void studentIdToDelete(@RequestParam int studentIdToDelete) {
        studentService.deleteById(studentIdToDelete);
    }


//    @PutMapping("/delete/{id}")
//    @ResponseBody
//    public String deleteStudentById(@PathVariable("id") int studentId) {
//        studentService.deleteStudent(studentId);
//        return "redirect:/students";
//    }

    @PutMapping("/bringBack/{id}")
    @ResponseBody
    public String bringBackStudentById(@PathVariable("id") int studentId) {
        studentService.bringBackStudent(studentId);
        return "redirect:/students";
    }

}
