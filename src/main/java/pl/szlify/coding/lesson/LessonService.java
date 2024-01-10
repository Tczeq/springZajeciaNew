package pl.szlify.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.coding.lesson.exception.InvalidDate;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.student.StudentRepository;
import pl.szlify.coding.student.StudentService;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.TeacherService;
import pl.szlify.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
//    private final LessonService lessonService;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Transactional
    public void create(Lesson lesson, int teacherId, int studentId) {
        LocalDateTime term = lesson.getTerm();
        if (term.isBefore(LocalDateTime.now())) {
            throw new InvalidDate("Term cannot be from the past ");
        }
        Teacher teacher = teacherRepository.findWithLockingById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", teacherId)));
        if (teacher.isFired()) {
            throw new EntityNotFoundException("Teacher is fired");
        }
        if (lessonRepository.existsByTeacherIdAndTermAfterAndTermBefore(teacherId, term.minusHours(1), term.plusHours(1))) {
            throw new InvalidDate("Term unavailable");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} not found", studentId)));
        lesson.setStudent(student);
        lesson.setTeacher(teacher);
        lessonRepository.save(lesson);
    }

    public Lesson findLessonById(int lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + lessonId + " not found"));
    }


    @Transactional
    public void update(Lesson updatedLesson) {
        Lesson existingLesson = lessonRepository.findById(updatedLesson.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + updatedLesson.getId() + " not found"));


//        Student student = studentService.findById(studentId);
//        Teacher teacher = teacherService.findTeacherById(teacherId);

//        existingLesson.setTerm(updatedLesson.getTerm());
//        existingLesson.setTeacher(updatedLesson.getTeacher());
//        existingLesson.setStudent(updatedLesson.getStudent());
//        existingLesson.setStudent(student);
//        existingLesson.setTeacher(teacher);
        existingLesson.setTerm(updatedLesson.getTerm());

        lessonRepository.save(existingLesson);
    }


    @Transactional
    public void deleteById(int idToDelete) {

        lessonRepository.deleteById(idToDelete);
    }
}
