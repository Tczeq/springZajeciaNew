package pl.szlify.coding.student;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.coding.common.exception.LanguageMismatchException;
import pl.szlify.coding.student.command.CreateStudentCommand;
import pl.szlify.coding.student.command.UpdateStudentCommand;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.student.model.dto.StudentDto;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public List<StudentDto> findAll(){
        return studentRepository.findAll().stream()
                .map(StudentDto::fromEntity)
                .toList();
    }

    public StudentDto create(CreateStudentCommand command, int teacherId){

        Student student = command.toEntity();


        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", teacherId)));
        if (!teacher.getLanguages().contains(student.getLanguage())) {
            throw new LanguageMismatchException(MessageFormat
                    .format("Language for teacher with id={0} not found", teacherId));
        }
        student.setTeacher(teacher);

        return StudentDto.fromEntity(studentRepository.save(student));
    }

    public List<StudentDto> findStudentsByTeacher(int teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", teacherId)));
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
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} not found", id)));
    }

    @Transactional
    public StudentDto update(int id, UpdateStudentCommand command) {
        Student student = studentRepository.findWithLockingById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} not found", id)));
        if(command.getFirstName() != null) {
            student.setFirstName(command.getFirstName());
        }
        if(command.getLastName() != null){
            student.setLastName(command.getLastName());
        }
        return StudentDto.fromEntity(student);
    }
}
