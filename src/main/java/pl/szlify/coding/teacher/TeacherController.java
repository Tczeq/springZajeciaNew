package pl.szlify.coding.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teacher/list";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("languages", Language.values());
        return "teacher/form";
    }

    @PostMapping("/create")
    public String create(Teacher teacher) {
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable("id") int teacherId, Model model) {
        Teacher teacher = teacherService.findTeacherById(teacherId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("languages", Language.values());
        return "teacher/form";
    }

    @PostMapping("/update")
    public String updateTeacher(Teacher teacher) {
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @DeleteMapping
    @ResponseBody
    public void teacherIdToDelete(@RequestParam int teacherIdToDelete) {
        teacherService.deleteById(teacherIdToDelete);
    }


    @GetMapping(params = "language")
    @ResponseBody
    public List<TeacherDto> getAll(@RequestParam Language language) {
//        return teacherService.findAllByLanguage(language).stream()
//                .map(TeacherDto::fromEntity)
//                .toList();
        return teacherService.findAllByLanguage(language);
    }


    @PutMapping("/fire/{id}")
    @ResponseBody
    public String firedTeacherById(@PathVariable("id") int teacherId) {
        teacherService.fireTeacher(teacherId);
        return "redirect:/teachers";
    }

    @PutMapping("/hire/{id}")
    @ResponseBody
    public String hiredTeacherById(@PathVariable("id") int teacherId) {
        teacherService.hireTeacher(teacherId);
        return "redirect:/teachers";
    }


}
