package pl.szlify.coding.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.coding.common.exception.LanguageMismatchException;
import pl.szlify.coding.student.exception.StudentNotFoundException;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.student.model.command.CreateStudentCommand;
import pl.szlify.coding.student.model.command.UpdateStudentCommand;
import pl.szlify.coding.student.model.dto.StudentDto;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.exception.TeacherNotFoundException;
import pl.szlify.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public List<StudentDto> findAll() {
        return studentRepository.findAll().stream()
                .map(StudentDto::fromEntity)
                .toList();
    }

    public StudentDto create(CreateStudentCommand command, int teacherId) {

        Student student = command.toEntity();

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
        if (!teacher.getLanguages().contains(student.getLanguage())) {
            throw new LanguageMismatchException(teacherId);
        }
        student.setTeacher(teacher);

        return StudentDto.fromEntity(studentRepository.save(student));
    }

    public List<StudentDto> findStudentsByTeacher(int teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
        return studentRepository.findAllByTeacher(teacher).stream()
                .map(StudentDto::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteById(int id) {
        studentRepository.deleteById(id);
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public StudentDto update(int id, UpdateStudentCommand command) {
        Student student = studentRepository.findWithLockingById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        if (command.getFirstName() != null) {
            student.setFirstName(command.getFirstName());
        }
        if (command.getLastName() != null) {
            student.setLastName(command.getLastName());
        }
        return StudentDto.fromEntity(student);
    }
}
