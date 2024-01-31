package pl.szlify.coding.teacher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.exception.TeacherNotFoundException;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.command.CreateTeacherCommand;
import pl.szlify.coding.teacher.model.command.UpdateTeacherCommand;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @Captor
    private ArgumentCaptor<Teacher> teacherArgumentCaptor;

//    @Test
//    void testFindAll_HappyPath_ResultsInTeacherFoundAllTeachers() {
//        //given
//        Teacher toFind = Teacher.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(Language.JAVA, Language.JS))
//                .build();
//
//
//        when(teacherRepository.findAll()).thenReturn(List.of(toFind));
//
//        //when
//        List<TeacherDto> actualStudents = teacherService.findAll();
//
//        //then
//        verify(teacherRepository).findAll();
//        TeacherDto expectedTeacherDto = TeacherDto.fromEntity(toFind);
//        assertEquals(List.of(expectedTeacherDto), actualStudents);
//    }


//    @Test
//    void testFindAll_NotFoundAll_ResultsInTeacherNotFoundAllTeachers() {
//        //given
//        Teacher toFind = Teacher.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(Language.JAVA, Language.JS))
//                .build();
//
//        when(teacherRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
//
//        //when
//        List<TeacherDto> actualTeachers = teacherService.findAll();
//
//        //then
//        verify(teacherRepository).findAll();
//        TeacherDto expected = TeacherDto.fromEntity(toFind);
//        assertNotEquals(List.of(expected), actualTeachers);
//    }

    @Test
    void testCreate_HappyPath_ResultsInTeacherBeingSaved() {
        //given
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.JS))
                .build();

        Teacher toSave = command.toEntity();
        when(teacherRepository.save(any(Teacher.class))).thenReturn(toSave);

        //when
        teacherService.create(command);

        //then
        verify(teacherRepository).save(teacherArgumentCaptor.capture());
        Teacher saved = teacherArgumentCaptor.getValue();

        assertEquals(toSave.getFirstName(), saved.getFirstName());
        assertEquals(toSave.getLastName(), saved.getLastName());
        assertEquals(toSave.getLanguages(), saved.getLanguages());
        assertEquals(toSave, saved);
    }


    @Test
    void testDeleteById_HappyPath_ResultsInTeacherFound() {
        //give
        int teacherId = 3;
        Teacher toFind = Teacher.builder()
                .id(teacherId)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.JS))
                .build();

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(toFind));

        //when
        teacherService.deleteById(teacherId);

        //then
        verify(teacherRepository).save(teacherArgumentCaptor.capture());
        Teacher savedTeacher = teacherArgumentCaptor.getValue();
        assertTrue(savedTeacher.isDeleted());
        assertEquals(toFind, savedTeacher);
    }


    @Test
    void testDeleteById_TeacherNotFoundById_ResultsTeacherNotFoundException() {
        //given
        int teacherId = 3;
        String exceptionMsg = MessageFormat.format("Teacher with id={0} not found.", teacherId);

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.empty());

        //when
        assertThatExceptionOfType(TeacherNotFoundException.class)
                .isThrownBy(() -> teacherService.deleteById(teacherId))
                .withMessage(exceptionMsg);
        verify(teacherRepository).findWithLockingById(teacherId);
    }


//    @Test
//    void testFindTeachersByLanguages_HappyPath_ResultsInTeachersFound() {
//        //given
//        Teacher toFind = Teacher.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .languages(Set.of(Language.JAVA, Language.JS))
//                .build();
//        Language language = Language.JAVA;
//
//        List<Teacher> teachers = List.of(toFind);
//
//        when(teacherRepository.findAllByLanguagesContaining(language)).thenReturn(teachers);
//
//        //when
//        List<TeacherDto> actualTeachers = teacherService.findAllByLanguage(language);
//        List<TeacherDto> expectedTeachers = teachers.stream()
//                .map(TeacherDto::fromEntity)
//                .toList();
//
//        //then
//        verify(teacherRepository).findAllByLanguagesContaining(language);
//        assertEquals(expectedTeachers, actualTeachers);
//    }

    @Test
    void testFindTeacherById_HappyPath_ResultsInTeacherFound() {
        //given
        int teacherId = 3;
        Teacher toFind = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.JS))
                .build();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(toFind));

        //when
        teacherService.findById(teacherId);

        //then
        verify(teacherRepository).findById(teacherId);
    }

    @Test
    void testFindTeacherById_TeacherNotFound_ResultsTeacherNotFoundException() {
        //given
        int teacherId = 3;
        String exceptionMsg = MessageFormat.format("Teacher with id={0} not found.", teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(TeacherNotFoundException.class)
                .isThrownBy(() -> teacherService.findById(teacherId))
                .withMessage(exceptionMsg);
        verify(teacherRepository).findById(teacherId);
    }


    @Test
    void testUpdate_HappyPath_ResultsInTeacherBeingUpdated() {
        // given
        int teacherId = 3;

        Teacher actualTeacher = Teacher.builder()
                .id(teacherId)
                .firstName("Test1")
                .lastName("Testowy1")
                .languages(Set.of(Language.C, Language.JS))
                .build();

        UpdateTeacherCommand toSave = UpdateTeacherCommand.builder()
                .firstName("Test2")
                .lastName("Testowy2")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.of(actualTeacher));

        // when
        TeacherDto updatedTeacherDto = teacherService.update(teacherId, toSave);

        // then
        verify(teacherRepository).findWithLockingById(teacherId);

        actualTeacher.setFirstName(toSave.getFirstName());
        actualTeacher.setLastName(toSave.getLastName());
        actualTeacher.setLanguages(toSave.getLanguages());

        assertEquals(actualTeacher.getId(), updatedTeacherDto.getId());
        assertEquals(actualTeacher.getFirstName(), updatedTeacherDto.getFirstName());
        assertEquals(actualTeacher.getLastName(), updatedTeacherDto.getLastName());
        assertEquals(actualTeacher.getLanguages(), updatedTeacherDto.getLanguages());
        assertEquals(actualTeacher.isDeleted(), updatedTeacherDto.isDeleted());
    }

    @Test
    void testUpdate_TeacherNotFound_ResultsTeacherNotFoundException() {
        //given
        int teacherId = 3;
        String exceptionMsg = MessageFormat.format("Teacher with id={0} not found.", teacherId);

        UpdateTeacherCommand toSave = UpdateTeacherCommand.builder()
                .firstName("Test2")
                .lastName("Testowy2")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();


        when(teacherRepository.findWithLockingById(teacherId)).thenReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(TeacherNotFoundException.class)
                .isThrownBy(() -> teacherService.update(teacherId, toSave))
                .withMessage(exceptionMsg);
        verify(teacherRepository).findWithLockingById(teacherId);
    }


}