package pl.szlify.coding.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.command.CreateTeacherCommand;
import pl.szlify.coding.teacher.model.command.UpdateTeacherCommand;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
  Takie dodanie w nawiasie wevEnvironment sprawia ze
  aplikacja zostaje uruchomiona na prawdziwym serwerze webowym
  @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
*/
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer serviceServer;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void testGetAll_ResultsInTeacherListBeingReturned() throws Exception {
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();
        Teacher teacher2 = Teacher.builder()
                .firstName("Test2")
                .lastName("Testowy2")
                .languages(Set.of(Language.C, Language.CPP))
                .build();
        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);

        mockMvc.perform(get("/api/v1/teachers"))
                .andDo(print())
                .andExpect(status().isOk());
        //todo: jsonPath
    }

    @Test
    void testGetById_ResultsInTeacherBeingReturned() throws Exception {
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();
        teacherRepository.save(teacher);

        int teacherId = teacher.getId();

        mockMvc.perform(get("/api/v1/teachers/" + teacherId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(teacherId)))
                .andExpect(jsonPath("$.firstName", Matchers.is("Test")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Testowy")));
    }

    @Test
    void testCreate_ResultsInTeacherBeingSaved() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", Matchers.is("Test")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Testowy")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsWrongInputFirstName() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", Matchers.is("firstName")))
                .andExpect(jsonPath("$.violations[0].message", Matchers.is("The name must begin with a capital letter and contain from 1 to 50 letters.")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsFirstNameNull() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", Matchers.is("firstName")))
                .andExpect(jsonPath("$.violations[0].message", Matchers.is("firstname is mandatory")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsWrongInputLastName() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .lastName("1estowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", Matchers.is("lastName")))
                .andExpect(jsonPath("$.violations[0].message", Matchers.is("The lastname must begin with a capital letter and contain from 1 to 50 letters.")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsLastNameNull() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", Matchers.is("lastName")))
                .andExpect(jsonPath("$.violations[0].message", Matchers.is("lastname is mandatory")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsInAtLeastOneLanguage() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .lastName("Testowy")
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", Matchers.is("languages")))
                .andExpect(jsonPath("$.violations[0].message", Matchers.is("At least one language")));
    }

    //nie dziala
    @Test
    void testUpdate_ResultsInTeacherBeingUpdated() throws Exception {
        UpdateTeacherCommand command = UpdateTeacherCommand.builder()
                .firstName("UpdateName")
                .lastName("UpdateLastName")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        teacherRepository.save(teacher);

        int teacherId = teacher.getId();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(put("/api/v1/teachers/" + teacherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(teacherId)))
                .andExpect(jsonPath("$.firstName", Matchers.is("UpdateName")))
                .andExpect(jsonPath("$.lastName", Matchers.is("UpdateLastName")));

    }

    @Test
    void testDelete_ResultsInTeacherBeingDeleted() throws Exception {
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();
        teacherRepository.save(teacher);

        int teacherId = teacher.getId();

        mockMvc.perform(delete("/api/v1/teachers/" + teacherId))
                .andDo(print())
                .andExpect(status().isOk());

        Teacher deletedTeacher = teacherRepository.findById(teacherId).orElse(null);
        assertNotNull(deletedTeacher);
        assertTrue(deletedTeacher.isDeleted());
    }

    @Test
    void testGetAllLanguages_ResultsInTeacherGotAllLanguages() throws Exception {
        Language java = Language.JAVA;
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(java, Language.PYTHON))
                .build();
        Teacher teacher2 = Teacher.builder()
                .firstName("Test2")
                .lastName("Testowy2")
                .languages(Set.of(Language.C, java))
                .build();
        Teacher teacher3 = Teacher.builder()
                .firstName("Test3")
                .lastName("Testowy3")
                .languages(Set.of(Language.C, Language.PYTHON))
                .build();

        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        mockMvc.perform(get("/api/v1/teachers?language=" + java))
                .andDo(print())
                .andExpect(status().isOk());

        //można też tak
//        MvcResult mvcResult = mockMvc.perform(get("/api/v1/teachers?language=" + java))
//                .andDo(print())
//                .andReturn();
//
//        assertEquals(200, mvcResult.getResponse().getStatus());

    }
}