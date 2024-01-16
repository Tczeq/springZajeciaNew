package pl.szlify.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.coding.lesson.exception.InvalidDate;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.lesson.model.command.CreateLessonCommand;
import pl.szlify.coding.lesson.model.command.UpdateLessonCommand;
import pl.szlify.coding.lesson.model.dto.LessonDto;
import pl.szlify.coding.student.StudentRepository;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public List<LessonDto> findAll() {
        return lessonRepository.findAll().stream()
                .map(LessonDto::fromEntity)
                .toList();
    }

//    @Transactional
//    public void create(Lesson lesson, int teacherId, int studentId) {
//        LocalDateTime term = lesson.getTerm();
//        if (term.isBefore(LocalDateTime.now())) {
//            throw new InvalidDate("Term cannot be from the past ");
//        }
//        Teacher teacher = teacherRepository.findWithLockingById(teacherId)
//                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
//                        .format("Teacher with id={0} not found", teacherId)));
//        if (lessonRepository.existsByTeacherIdAndTermAfterAndTermBefore(teacherId, term.minusHours(1), term.plusHours(1))) {
//            throw new InvalidDate("Term unavailable");
//        }
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
//                        .format("Student with id={0} not found", studentId)));
//        lesson.setStudent(student);
//        lesson.setTeacher(teacher);
//        lessonRepository.save(lesson);
//    }

    @Transactional
    public LessonDto create(CreateLessonCommand command, int teacherId, int studentId) {

        Lesson lesson = command.toEntity();

        LocalDateTime term = lesson.getTerm();
        if (term.isBefore(LocalDateTime.now())) {
            throw new InvalidDate("Term cannot be from the past ");
        }
        Teacher teacher = teacherRepository.findWithLockingById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", teacherId)));
        if (lessonRepository.existsByTeacherIdAndTermAfterAndTermBefore(teacherId, term.minusHours(1), term.plusHours(1))) {
            throw new InvalidDate("Term unavailable");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} not found", studentId)));
        lesson.setStudent(student);
        lesson.setTeacher(teacher);

        return LessonDto.fromEntity(lessonRepository.save(lesson));
    }

//    public Lesson findLessonById(int lessonId) {
//
//        Lesson lesson = lessonRepository.findById(lessonId)
//                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + lessonId + " not found"));
//
//        boolean beforeTerm = lesson.getTerm().isBefore(LocalDateTime.now());
//        boolean afterTerm = lesson.getTerm().plusHours(1).isAfter(LocalDateTime.now());
//
//        if (beforeTerm && afterTerm) {
//            throw new InvalidDate("Lesson already started");
//        }
//
//        return lesson;
//    }

    //FIXME
    @Transactional
    public LessonDto update(int id, UpdateLessonCommand command) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Lesson with id={0} not found", id)));
        if (command.getTerm() != null) {
            lesson.setTerm(command.getTerm());
        }
        return LessonDto.fromEntity(lesson);
    }


    @Transactional
    public void deleteById(int id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Lesson with id={0} not found", id)));
        if (lesson.getIsDeleted() == null) {
            lesson.setIsDeleted(false);
        }
        lessonRepository.deleteById(id);
    }
}
