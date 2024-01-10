package pl.szlify.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.lesson.exception.InvalidDate;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.student.StudentRepository;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.TeacherRepository;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void testFindAll_HappyPath_ResultsInLessonFindAllLessons() {
        //given

        LocalDateTime localDateTime = LocalDateTime.of(2022, 12, 30, 12, 12);

        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Teacher teacher = Teacher.builder()
                .languages(Set.of(student.getLanguage()))
                .build();
        Lesson toFind = Lesson.builder()
                .term(localDateTime)
                .student(student)
                .teacher(teacher)
                .build();

        when(lessonRepository.findAll()).thenReturn(List.of(toFind));

        //when
        List<Lesson> actualLessons = lessonService.findAll();

        //then
        verify(lessonRepository).findAll();
        assertEquals(List.of(toFind), actualLessons);
    }


    @Test
    void testFindAll_UnHappyPath_ResultsInTeacherNotFoundAllTeachers() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2022, 12, 30, 12, 12);

        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Teacher teacher = Teacher.builder()
                .languages(Set.of(student.getLanguage()))
                .build();
        Lesson toFind = Lesson.builder()
                .term(localDateTime)
                .student(student)
                .teacher(teacher)
                .build();

        when(lessonRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //when
        List<Lesson> actualLessons = lessonService.findAll();

        //then
        verify(lessonRepository).findAll();
        assertNotEquals(List.of(toFind), actualLessons);
    }

    @Test
    void testCreate_HappyPath_ResultsInLessonBeingSaved() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 12, 30, 12, 12);
        int teacherId = 1;
        int studentId = 1;
        Student student = Student.builder()
                .id(studentId)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .languages(Set.of(student.getLanguage()))
                .build();
        Lesson toSave = Lesson.builder()
                .term(localDateTime)
                .student(student)
                .teacher(teacher)
                .build();

        when(teacherRepository.findWithLockingById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(studentRepository.findWithLockingById(student.getId())).thenReturn(Optional.of(student));

        //when
        lessonService.create(toSave, teacherId, studentId);

        //then
        verify(teacherRepository).findWithLockingById(teacherId);
        verify(studentRepository).findWithLockingById(studentId);
        verify(lessonRepository).save(lessonArgumentCaptor.capture());

        Lesson saved = lessonArgumentCaptor.getValue();
        assertEquals(student, saved.getStudent());
        assertEquals(teacher, saved.getTeacher());
        assertEquals(localDateTime, saved.getTerm());
        assertEquals(toSave, saved);
    }

    @Test
    void testCreate_UnHappyPath_ResultsInLessonNotBeingSaved_WhenTeacherNotFound() {

        //given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 12, 30, 12, 12);
        int teacherId = 1;
        int studentId = 1;

        Lesson toSave = Lesson.builder()
                .term(localDateTime)
                .build();

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> {
            //when
            lessonService.create(toSave, teacherId, studentId);
        });
    }

    @Test
    void testCreate_UnHappyPath_ResultsInLessonNotBeingSaved_WhenStudentNotFound() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 12, 30, 12, 12);
        int teacherId = 1;
        int studentId = 1;

        Lesson toSave = Lesson.builder()
                .term(localDateTime)
                .build();
        Student student = Student.builder()
                .id(studentId)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .languages(Set.of(student.getLanguage()))
                .build();

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findWithLockingById(studentId)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> {
            //when
            lessonService.create(toSave, teacherId, studentId);
        });
    }

    @Test
    void testCreate_UnHappyPath_ResultsInLessonNotBeingSaved_WhenTermIsFromPast() {

        LocalDateTime pastTerm = LocalDateTime.now().minusDays(1);
        int teacherId = 1;
        int studentId = 1;

        Lesson toSave = Lesson.builder()
                .term(pastTerm)
                .build();

        //then
        assertThrows(InvalidDate.class, () -> {
            //when
            lessonService.create(toSave, teacherId, studentId);
        });
    }

    @Test
    void testCreate_UnHappyPath_ResultsInLessonNotBeingSaved_WhenTeacherIsFired() {

        //given
        LocalDateTime pastTerm = LocalDateTime.now().minusDays(1);

        LocalDateTime localDateTime = LocalDateTime.of(2023, 12, 30, 12, 12);
        int teacherId = 1;
        int studentId = 1;
        Student student = Student.builder()
                .id(studentId)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .languages(Set.of(student.getLanguage()))
                .fired(true)
                .build();
        Lesson toSave = Lesson.builder()
                .term(localDateTime)
                .build();

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(teacher));

        //then
        assertThrows(EntityNotFoundException.class, () -> {
            //when
            lessonService.create(toSave, teacherId, studentId);
        });
    }

    @Test
    void testCreate_UnHappyPath_ResultsInLessonNotBeingSaved_WhenTermIsNotAvailable() {

        //given
        LocalDateTime term = LocalDateTime.of(2023, 12, 30, 12, 12);
        int teacherId = 1;
        int studentId = 1;
        Student student = Student.builder()
                .id(studentId)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .languages(Set.of(student.getLanguage()))
                .build();
        Lesson toSave = Lesson.builder()
                .term(term)
                .build();

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(teacher));
        when(lessonRepository.existsByTeacherIdAndTermAfterAndTermBefore(teacherId, term.minusHours(1), term.plusHours(1))).thenReturn(true);

        //then
        assertThrows(InvalidDate.class, () -> {
            //when
            lessonService.create(toSave, teacherId, studentId);
        });
    }

    @Test
    void testFindTeacherById_HappyPath_ResultsInTeacherFound() {
        //given
        int lessonId = 3;
        LocalDateTime term = LocalDateTime.of(2023, 12, 30, 12, 12);

        Lesson toFind = Lesson.builder()
                .term(term)
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(toFind));
        //when
        lessonService.findLessonById(lessonId);

        //then
        verify(lessonRepository).findById(lessonId);
    }

    @Test
    void testFindTeacherById_UnHappyPath_ResultsInTeacherNotFound() {
        //given
        int lessonId = 3;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> {
            //when
            lessonService.findLessonById(lessonId);
        });
    }

    @Test
    void testDeleteById_HappyPath_ResultsInTeacherFound() {
        int lessonID = 3;

        lessonService.deleteById(lessonID);

        verify(lessonRepository).deleteById(lessonID);

    }

    @Test
    void testDeleteById_UnHappyPath_ResultsInTeacherNotFound() {
        int lessonId = 3;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        lessonService.deleteById(lessonId);

        //then
        assertThrows(EntityNotFoundException.class, () -> {
            //when
            lessonService.findLessonById(lessonId);
        });

        verify(lessonRepository).deleteById(lessonId);

    }

    @Test
    void testUpdate_HappyPath_ResultsInLessonBeingUpdated() {
        //given
        int lessonId = 3;
        LocalDateTime term = LocalDateTime.of(2023, 12, 30, 12, 12);

        Lesson actualLesson = Lesson.builder()
                .id(lessonId)
                .term(term.minusDays(1))
                .build();

        Lesson toUpdate = Lesson.builder()
                .id(lessonId)
                .term(term)
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(actualLesson));

        //when
        lessonService.update(toUpdate);

        //then
        verify(lessonRepository).findById(lessonId);
        verify(lessonRepository).save(lessonArgumentCaptor.capture());

        Lesson saved = lessonArgumentCaptor.getValue();
        assertEquals(toUpdate.getTerm(), saved.getTerm());
        assertEquals(toUpdate.getStudent(), saved.getStudent());
        assertEquals(toUpdate.getTeacher(), saved.getTeacher());
        assertEquals(lessonId, saved.getId());
    }

    @Test
    void testUpdate_UnHappyPath_ResultsInLessonNotUpdated() {
        //given
        int lessonId = 3;
        LocalDateTime term = LocalDateTime.of(2023, 12, 30, 12, 12);

        Lesson toUpdate = Lesson.builder()
                .id(lessonId)
                .term(term)
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> {
            //when
            lessonService.update(toUpdate);
        });

    }
}