package pl.szlify.coding.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.coding.lesson.exception.LessonNotFoundException;
import pl.szlify.coding.lesson.exception.PastTermException;
import pl.szlify.coding.lesson.exception.TermUnavailableException;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.lesson.model.command.CreateLessonCommand;
import pl.szlify.coding.lesson.model.command.UpdateLessonCommand;
import pl.szlify.coding.lesson.model.dto.LessonDto;
import pl.szlify.coding.student.StudentRepository;
import pl.szlify.coding.student.exception.StudentNotFoundException;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.exception.TeacherNotFoundException;
import pl.szlify.coding.teacher.model.Teacher;

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

    @Transactional
    public LessonDto create(CreateLessonCommand command, int teacherId, int studentId) {
        LocalDateTime term = command.getTerm();
        if (term.isBefore(LocalDateTime.now())) {
            throw new PastTermException("Term cannot be from the past ");
        }

        Teacher teacher = teacherRepository.findWithLockingById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
        if (lessonRepository.existsByTeacherIdAndTermAfterAndTermBefore(teacherId, term.minusHours(1), term.plusHours(1))) {
            throw new TermUnavailableException("Term unavailable");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Lesson lesson = command.toEntity();
        lesson.setStudent(student);
        lesson.setTeacher(teacher);


        Lesson save = lessonRepository.save(lesson);
        return LessonDto.fromEntity(save);
    }

    public LessonDto update(int id, UpdateLessonCommand command) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
        if (command.getTerm().isBefore(LocalDateTime.now())) {                 // to dodalem
            throw new PastTermException("Term cannot be from the past ");
        }
        if (command.getTerm() != null) {
            lesson.setTerm(command.getTerm());
        }
        lessonRepository.save(lesson);
        return LessonDto.fromEntity(lesson);
    }


    @Transactional
    public void deleteById(int id) {
        lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
        lessonRepository.deleteById(id);
    }
}
