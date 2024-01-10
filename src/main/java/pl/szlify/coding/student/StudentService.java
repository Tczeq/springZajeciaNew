package pl.szlify.coding.student;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.coding.common.exception.LanguageMismatchException;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.student.model.dto.StudentDto;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    @Transactional
    public void create(Student student, int teacherId){
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", teacherId)));
        if (!teacher.getLanguages().contains(student.getLanguage())) {
            throw new LanguageMismatchException(MessageFormat
                    .format("Language for teacher with id={0} not found", teacherId));
        }
        student.setTeacher(teacher);
        studentRepository.save(student);
    }

    public List<StudentDto> findStudentsByTeacher(int teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id=" + teacherId + " not found"));
        return studentRepository.findAllByTeacher(teacher).stream()
                .map(StudentDto::fromEntity)
                .toList();
    }



    @Transactional
    public void deleteById(int idToDelete) {
        studentRepository.deleteById(idToDelete);
    }

    public Student findById(int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with id=" + studentId + " not found"));
    }


//    @Transactional
//    public void deleteStudent(int studentId){
//        Student student = findStudentById(studentId);
//        student.setDeleted(true);
//        studentRepository.save(student);
//
//    }


    @Transactional
    public void bringBackStudent(int studentId) {
        Student student = findById(studentId);
        student.setDeleted(false);
        studentRepository.save(student);
    }
}
