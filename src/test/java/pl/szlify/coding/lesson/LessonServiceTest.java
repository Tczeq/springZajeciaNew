package pl.szlify.coding.lesson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.lesson.model.command.CreateLessonCommand;
import pl.szlify.coding.lesson.model.dto.LessonDto;
import pl.szlify.coding.student.StudentRepository;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonService lessonService;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    @Captor
    private ArgumentCaptor<Lesson> lessonArgumentCaptor;

//    @Test
//    void testFindAll_HappyPath_ResultsInLessonFindAllLessons() {
//        //given
//        LocalDateTime localDateTime = LocalDateTime.of(2022, 12, 30, 12, 12);
//
//        Student student = Student.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .languages(Set.of(student.getLanguage()))
//                .build();
//        Lesson toFind = Lesson.builder()
//                .term(localDateTime)
//                .student(student)
//                .teacher(teacher)
//                .build();
//
//        when(lessonRepository.findAll()).thenReturn(List.of(toFind));
//
//        //when
//        List<LessonDto> actualLessons = lessonService.findAll();
//
//        //then
//        verify(lessonRepository).findAll();
//        LessonDto expected = LessonDto.fromEntity(toFind);
//        assertEquals(List.of(expected), actualLessons);
//    }

    @Test
    void testCreate_HappyPath_ResultsInLessonBeingSaved() {
        //given
        int teacherId = 1;
        int studentId = 1;
        int expectedLessonId = 2;
        Student student = Student.builder()
                .id(studentId)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(student.getLanguage()))
                .build();
        CreateLessonCommand command = CreateLessonCommand.builder()
                .term(LocalDateTime.now().plusDays(1))
                .studentId(studentId)
                .teacherId(teacherId)
                .build();

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(args -> {
            Lesson toReturn = args.getArgument(0);
            toReturn.setId(expectedLessonId);
            return toReturn;
        });

        //when
        LessonDto returnedDto = lessonService.create(command, teacherId, studentId);

        //then
        verify(teacherRepository).findWithLockingById(teacherId);
        verify(studentRepository).findById(studentId);

        verify(lessonRepository).save(lessonArgumentCaptor.capture());
        Lesson saved = lessonArgumentCaptor.getValue();
        assertEquals(student, saved.getStudent());
        assertEquals(teacher, saved.getTeacher());
        assertEquals(command.getTerm(), saved.getTerm());
        assertFalse(saved.isDeleted());

        assertEquals(expectedLessonId, returnedDto.getId());
        assertEquals(command.getTerm(), returnedDto.getTerm());
        assertEquals(command.getTeacherId(), returnedDto.getTeacherId());
        assertEquals(command.getStudentId(), returnedDto.getStudentId());
        assertFalse(returnedDto.isDeleted());

    }

//    @Test
//    void testCreate_TeacherNotFound_ResultsTeacherNotFoundException() {
//        //given
//        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
//        int teacherId = 1;
//        int studentId = 1;
////        String exceptionMsg = MessageFormat.format("Teacher with id={0} not found", teacherId);
//        Student student = Student.builder()
//                .id(studentId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .id(teacherId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(student.getLanguage()))
//                .build();
//        CreateLessonCommand command = CreateLessonCommand.builder()
//                .term(localDateTime)
//                .student(student)
//                .teacher(teacher)
//                .build();
//
//        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.empty());
//
//        //when
//        assertThatExceptionOfType(TeacherNotFoundException.class)
//                .isThrownBy(() -> lessonService.create(command, teacherId, studentId));
//        verify(teacherRepository).findWithLockingById(teacherId);
//    }
//
//    @Test
//    void testCreate_StudentNotFound_ResultsInStudentNotFoundException() {
//        //given
//        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
//        int teacherId = 1;
//        int studentId = 1;
////        String exceptionMsg = MessageFormat.format("Student with id={0} not found", studentId);
//        Student student = Student.builder()
//                .id(studentId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .id(teacherId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(student.getLanguage()))
//                .build();
//        CreateLessonCommand command = CreateLessonCommand.builder()
//                .term(localDateTime)
//                .student(student)
//                .teacher(teacher)
//                .build();
//
//        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(teacher));
//        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
//
//        //when
//        assertThatExceptionOfType(StudentNotFoundException.class)
//                .isThrownBy(() -> lessonService.create(command, teacherId, studentId));
//        verify(studentRepository).findById(studentId);
//    }

//
//    @Test
//    void testCreate_TermFromPast_ResultsInPastTermException() {
//        //given
//        LocalDateTime pastTerm = LocalDateTime.now().minusDays(1);
//        int studentId = 1;
//        int teacherId = 1;
//
//        CreateLessonCommand command = CreateLessonCommand.builder()
//                .term(pastTerm)
//                .build();
//
//        //when //then
//        assertThatExceptionOfType(PastTermException.class)
//                .isThrownBy(() -> lessonService.create(command, studentId, teacherId));
//    }
//
//    @Test
//    void testCreate_TermUnavailable_ResultsInTermUnavailableException() {
//        //given
//        LocalDateTime term = LocalDateTime.now().plusDays(1);
//        int studentId = 1;
//        int teacherId = 1;
//        Student student = Student.builder()
//                .id(studentId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .id(teacherId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(student.getLanguage()))
//                .build();
//        CreateLessonCommand command = CreateLessonCommand.builder()
//                .term(term)
//                .build();
//
//        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(teacher));
//        when(lessonRepository.existsByTeacherIdAndTermAfterAndTermBefore(
//                teacherId, term.minusHours(1), term.plusHours(1))).thenReturn(true);
//
//        //when //then
//        assertThatExceptionOfType(TermUnavailableException.class)
//                .isThrownBy(() -> lessonService.create(command, studentId, teacherId));
//    }
//
//    @Test
//    void testDeleteById_HappyPath_ResultsInTeacherFound() {
//
//        //given
//        LocalDateTime localDateTime = LocalDateTime.of(2025, 12, 30, 12, 12);
//        int lessonID = 3;
//        int teacherId = 1;
//        int studentId = 1;
//        Student student = Student.builder()
//                .id(studentId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .id(teacherId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(student.getLanguage()))
//                .build();
//        Lesson lessonToFind = Lesson.builder()
//                .id(lessonID)
//                .term(localDateTime)
//                .student(student)
//                .teacher(teacher)
//                .build();
//
//        when(lessonRepository.findById(lessonID)).thenReturn(Optional.of(lessonToFind));
//
//        //when
//        lessonService.deleteById(lessonID);
//
//        //then
//        verify(lessonRepository).deleteById(lessonID);
//    }
//
//
//    @Test
//    void testUpdate_HappyPath_ResultsInLessonBeingUpdated() {
//        //given
//        int lessonId = 3;
//        int teacherId = 1;
//        int studentId = 1;
//        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
//        LocalDateTime toUpdateTime = LocalDateTime.now().plusDays(2);
//        Student student = Student.builder()
//                .id(studentId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .id(teacherId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(student.getLanguage()))
//                .build();
//        Lesson lessonToFind = Lesson.builder()
//                .id(lessonId)
//                .term(localDateTime)
//                .student(student)
//                .teacher(teacher)
//                .build();
//
//        UpdateLessonCommand command = UpdateLessonCommand.builder()
//                .term(toUpdateTime)
//                .build();
//        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonToFind));
//
//        //when
//        LessonDto update = lessonService.update(lessonId, command);
//
//        //then
//        verify(lessonRepository).save(lessonArgumentCaptor.capture());
//        Lesson updatedLesson = lessonArgumentCaptor.getValue();
//
//        assertNotNull(updatedLesson);
//        assertEquals(toUpdateTime, update.getTerm());
//        verify(lessonRepository).findById(lessonId);
//    }
//
//    @Test
//    void testUpdate_TermFromPast_ResultsInPastTermException() {
//        //given
//        int lessonId = 3;
//        int teacherId = 1;
//        int studentId = 1;
//        LocalDateTime toUpdateTime = LocalDateTime.now().minusDays(1);
//        Student student = Student.builder()
//                .id(studentId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .build();
//        Teacher teacher = Teacher.builder()
//                .id(teacherId)
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(student.getLanguage()))
//                .build();
//        Lesson lessonToFind = Lesson.builder()
//                .id(lessonId)
//                .term(toUpdateTime)
//                .student(student)
//                .teacher(teacher)
//                .build();
//        UpdateLessonCommand command = UpdateLessonCommand.builder()
//                .term(toUpdateTime)
//                .build();
//        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonToFind));
//
//        //when //then
//        assertThatExceptionOfType(PastTermException.class)
//                .isThrownBy(() -> lessonService.update(lessonId, command));
//        verify(lessonRepository).findById(lessonId);
//        verify(lessonRepository, never()).save(any(Lesson.class));
//    }

}