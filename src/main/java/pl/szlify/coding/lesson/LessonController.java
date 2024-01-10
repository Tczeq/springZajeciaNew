package pl.szlify.coding.lesson;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.lesson.exception.InvalidDate;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.teacher.TeacherService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final TeacherService teacherService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("lessons", lessonService.findAll());
        return "lesson/list";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        model.addAttribute("languages", Language.values());
        model.addAttribute("teachers", teacherService.findAll());
        return "lesson/form";
    }


    /*
     * Dodaje tutaj Model, aby mozna bylo po froncie wyswietlac wiadomosci
     * */
    @PostMapping("/create")
    public String create(Lesson lesson, @RequestParam int teacherId, @RequestParam int studentId, Model model) {
        try {
            lessonService.create(lesson, teacherId, studentId);
            return "redirect:/lessons";
        } catch (InvalidDate e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "lesson/form";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/lessons";
        }

    }


    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable("id") int lessonId, Model model) {
        Lesson lesson = lessonService.findLessonById(lessonId);
        model.addAttribute("lesson", lesson);
//        model.addAttribute("teachers", teacherService.findAll());
//        model.addAttribute("students", studentService.findAll());
        return "lesson/update";
    }


    @PostMapping("/update")
    public String updateLesson(Lesson lesson) {
//        Lesson existingLesson = lessonService.findLessonById(lesson.getId());
//        Student student = studentService.findById(studentId);
//        Teacher teacher = teacherService.findTeacherById(teacherId);
//        existingLesson.setStudent(student);
//        existingLesson.setTeacher(teacher);
//        existingLesson.setTerm(lesson.getTerm());
        lessonService.update(lesson);
        return "redirect:/lessons";
    }


    @DeleteMapping
    @ResponseBody
    public void deleteById(@RequestParam int idToDelete) {
        Lesson lesson = lessonService.findLessonById(idToDelete);
        if(lesson.getTerm().isBefore(LocalDateTime.now()) && lesson.getTerm().plusHours(1).isAfter(LocalDateTime.now())) {
            throw new InvalidDate("Lesson already started");
        }
        lessonService.deleteById(idToDelete);
    }



}
