package pl.szlify.coding.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.common.exception.LanguageMismatchException;
import pl.szlify.coding.student.exception.StudentNotFoundException;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.student.model.command.CreateStudentCommand;
import pl.szlify.coding.student.model.dto.StudentDto;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.exception.TeacherNotFoundException;
import pl.szlify.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //zaprzęga do pracy Mockito (@Mock oraz @InjectMocks)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;

    //praktycznie ten sam efekt co metodą poniżem możemy uzyskać używając adnotacji @Mock oraz @InjectMocks
//    @BeforeEach
//    void init() {
//        studentRepository = mock(StudentRepository.class);
//        teacherRepository = mock(TeacherRepository.class);
//        studentService = new StudentService(studentRepository, teacherRepository);
//    }


    @Test
    void testCreate_HappyPath_ResultsInStudentBeingSaved() {
        //given
        int teacherId = 2;

        CreateStudentCommand command = CreateStudentCommand.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();

        Student student = command.toEntity();
        Teacher teacher = Teacher.builder()
                .languages(Set.of(student.getLanguage()))
                .build();

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        //when
        studentService.create(command, teacherId);

        //then
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student saved = studentArgumentCaptor.getValue();

        assertEquals(student.getFirstName(), saved.getFirstName());
        assertEquals(student.getLastName(), saved.getLastName());
        assertEquals(student.getLanguage(), saved.getLanguage());
        assertEquals(teacher, saved.getTeacher());
    }


    @Test
    void testCreate_TeacherNotFound_ResultsInTeacherNotFoundException() {
        //given
        int teacherId = 2;
        String exceptionMsg = MessageFormat.format("Teacher with id={0} not found.", teacherId);
        CreateStudentCommand command = CreateStudentCommand.builder()
                .firstName("Tes")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(TeacherNotFoundException.class)
                .isThrownBy(() -> studentService.create(command, teacherId))
                .withMessage(exceptionMsg);

        verify(teacherRepository).findById(teacherId);
        verifyNoInteractions(studentRepository);
    }

    @Test
    void testCreate_LanguageMismatch_ResultsInLanguageMismatchException() {
        //given
        int teacherId = 2;
        String exceptionMsg = MessageFormat.format("Language for teacher with id={0} not found", teacherId);

        CreateStudentCommand command = CreateStudentCommand.builder()
                .firstName("Tes")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();

        Teacher teacher = Teacher.builder()
                .languages(Set.of(Language.PYTHON))
                .build();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        //when //then
        assertThatExceptionOfType(LanguageMismatchException.class)
                .isThrownBy(() -> studentService.create(command, teacherId))
                .withMessage(exceptionMsg);

        verify(teacherRepository).findById(teacherId);
        verifyNoInteractions(studentRepository);
    }

    @Test
    void testFindStudentById_HappyPath_ResultsInStudentBeingReturned() {
        //given
        int studentId = 3;
        Student toFind = Student.builder()
                .id(studentId)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(toFind));

        //when
        Student found = studentService.findById(studentId);

        //then
        assertEquals(toFind.getId(), found.getId());
        assertEquals(toFind.getFirstName(), found.getFirstName());
        assertEquals(toFind.getLastName(), found.getLastName());

        verify(studentRepository).findById(studentId);
    }

    @Test
    void testFindStudentById_StudentNotFound_ResultsInStudentNotFoundException() {
        //given
        int studentId = 3;
        String exceptionMsg = MessageFormat.format("Student with id={0} not found.", studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.findById(studentId))
                .withMessage(exceptionMsg);

        verify(studentRepository).findById(studentId);
    }


//    @Test
//    void testFindStudentByTeacher_HappyPath_ResultsInStudentFound() {
//        //given
//        int teacherId = 3;
//        Student toFind = Student.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .id(teacherId)
//                .languages(Set.of(toFind.getLanguage()))
//                .build();
//
//        List<Student> students = List.of(toFind);
//        StudentDto studentDto = StudentDto.fromEntity(toFind);
//
//        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
//        when(studentRepository.findAllByTeacher(teacher)).thenReturn(students);
//
//        //when
//        List<StudentDto> actualStudents = studentService.findStudentsByTeacher(teacherId);
//
//        //then
//        verify(studentRepository).findAllByTeacher(teacher);
//        verify(teacherRepository).findById(teacherId);
//        assertFalse(actualStudents.isEmpty());
//        assertEquals(students.size(), actualStudents.size());
//        assertEquals(studentDto.getFirstName(), actualStudents.get(0).getFirstName());
//        assertEquals(studentDto.getLastName(), actualStudents.get(0).getLastName());
//    }


    @Test
    void testDeleteById_HappyPath_ResultsInStudentDeleted() {
        //given
        int studentId = 3;
        //when
        studentService.deleteById(studentId);
        //then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void testDeleteById_StudentNotFound_ResultsInStudentNotFoundException() {
        //given
        int studentId = 3;
        String exceptionMsg = MessageFormat.format("Student with id={0} not found.", studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //when
        studentService.deleteById(studentId);

        assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.findById(studentId))
                .withMessage(exceptionMsg);
        //then
        verify(studentRepository).deleteById(studentId);
    }


//    @Test
//    void testFindAll_HappyPath_ResultsInStudentFoundAllStudents() {
//        //given
//        Student toFind = Student.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        List<Student> studentsFromRepo = List.of(toFind);
//
//        when(studentRepository.findAll()).thenReturn(studentsFromRepo);
//
//        //when
//        List<StudentDto> actualStudents = studentService.findAll();
//
//        //then
//        verify(studentRepository).findAll();
//
//        //TODO zle
//        List<StudentDto> expectedStudentsDto = studentsFromRepo.stream()
//                .map(StudentDto::fromEntity)
//                .toList();
//        assertEquals(expectedStudentsDto, actualStudents);
//    }
}