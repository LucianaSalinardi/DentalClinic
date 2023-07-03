package ar.com.dh.SistemaDeReservas.integration;

import ar.com.dh.dtos.JsonMessageDto;
import ar.com.dh.dtos.requests.AddressRequestDto;
import ar.com.dh.dtos.requests.PatientRequestDto;
import ar.com.dh.entities.Address;
import ar.com.dh.entities.Patient;
import ar.com.dh.repositories.IPatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PatientControllerTest {

    @Autowired
    public MockMvc mockMvc;
   @MockBean
    public IPatientRepository patientRepository;

    @Test
    @DisplayName("Create patient if not exists-  method controller ")
    void createPatientIfNotExists() throws Exception {

        JsonMessageDto jsonMessageResponse = new JsonMessageDto("Successfully created patient", HttpStatus.CREATED.value());
        PatientRequestDto patientRequestDto =
                new PatientRequestDto(7L, "Juan", "Perez", "87609843", new AddressRequestDto(3L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        when(patientRepository.findByDni(patientRequestDto.getDni())).thenReturn((Optional.empty()));


        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        String patientJSON = writer.writeValueAsString(patientRequestDto);
        String jsonResponse = writer.writeValueAsString(jsonMessageResponse);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.post("/clinical-dental/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJSON))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(jsonPath("$.message").value("Successfully created patient"))
                        .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                        .andReturn();

        Assertions.assertEquals(jsonResponse , mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Can not create patient if dni contains letters-  method controller ")
    void createPatientMethodThrowsRequestValidationException() throws Exception {

        JsonMessageDto jsonMessageResponse = new JsonMessageDto("The dni must only contain numbers", HttpStatus.BAD_REQUEST.value());
        PatientRequestDto patientRequestDto =
                new PatientRequestDto(7L, "Juan", "Perez", "fhfgjjhg", new AddressRequestDto(3L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        String patientJSON = writer.writeValueAsString(patientRequestDto);
        String jsonResponse = writer.writeValueAsString(jsonMessageResponse);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.post("/clinical-dental/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patientJSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(jsonPath("$.message").value("The dni must only contain numbers"))
                        .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                        .andReturn();

        Assertions.assertEquals(jsonResponse , mvcResult.getResponse().getContentAsString());
    }


    @Test
    @DisplayName("Create patient method controller - Throws exception if patient already exists")
    void createPatientMethodThrowsResourceAlreadyExistsException() throws Exception {

        JsonMessageDto jsonMessageResponse = new JsonMessageDto("The patient already exists",HttpStatus.CONFLICT.value());
        PatientRequestDto patientRequestDto =
                new PatientRequestDto(7L, "Juan", "Perez", "87609843", new AddressRequestDto(3L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(3L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(7L, "Juan", "Perez", "87609843", date, address);

        when(patientRepository.findByDni(patientRequestDto.getDni())).thenReturn(Optional.of(patientEntity));


        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        String patientJSON = writer.writeValueAsString(patientRequestDto);
        String jsonResponse = writer.writeValueAsString(jsonMessageResponse);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.post("/clinical-dental/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patientJSON))
                        .andDo(print())
                        .andExpect(status().isConflict())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The patient already exists"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                        .andReturn();

        Assertions.assertEquals(jsonResponse , mvcResult.getResponse().getContentAsString());
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Get one patient method controller - Throws exception if patient does not exists")
    void getPatientMethodThrowsResourceNotFoundException() throws Exception {

        Long id = 7L;

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.get("/clinical-dental/patients/{id}", id))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The patient does not exists"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                        .andReturn();

        Assertions.assertEquals("application/json" , mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("Get One patient if exists - method controller")
    void getPatientIfExists() throws Exception {

        Long id = 5L;

        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(7L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(5L, "Juan", "Perez", "8765439", date, address);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patientEntity));

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.get("/clinical-dental/patients/{id}", id))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.idPatient").value(5))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Juan"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Perez"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.dni").value("8765439"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].street").value("Corrientes"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].number").value(345))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].town").value("CABA"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].province").value("Buenos Aires"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].postalCode").value(1413))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].country").value("Argentina"))
                        .andReturn();

        Assertions.assertEquals("application/json" , mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("Update patient method controller - Throws exception if patient or address does not exists")
    void updateMethodThrowsResourceNotFoundException() throws Exception {

        JsonMessageDto jsonMessageResponse = new JsonMessageDto("The patient associated with that address does not exists",HttpStatus.NOT_FOUND.value());
        PatientRequestDto patientRequestDto =
                new PatientRequestDto(7L, "Pedro", "Perez", "87609843", new AddressRequestDto(3L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));


        when(patientRepository.findByPatientIdAndAddressId(patientRequestDto.getIdPatient(),patientRequestDto.getAddress().getIdAddress())).thenReturn(Optional.empty());


        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        String patientJSON = writer.writeValueAsString(patientRequestDto);
        String jsonResponse = writer.writeValueAsString(jsonMessageResponse);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.put("/clinical-dental/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patientJSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The patient associated with that address does not exists"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                        .andReturn();

        Assertions.assertEquals(jsonResponse , mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Update patient method controller ")
    void updatePatientIfExists() throws Exception {

        JsonMessageDto jsonMessageResponse = new JsonMessageDto("Successfully modified patient",HttpStatus.OK.value());
        PatientRequestDto patientRequestDto =
                new PatientRequestDto(7L, "Pedro", "Perez", "87609843", new AddressRequestDto(9L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(9L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(7L, "Pedro", "Perez", "87609843", date, address);

        when(patientRepository.findByPatientIdAndAddressId(patientRequestDto.getIdPatient(),patientRequestDto.getAddress().getIdAddress())).thenReturn(Optional.of(patientEntity));


        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        String patientJSON = writer.writeValueAsString(patientRequestDto);
        String jsonResponse = writer.writeValueAsString(jsonMessageResponse);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.put("/clinical-dental/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patientJSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully modified patient"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                        .andReturn();

        Assertions.assertEquals(jsonResponse , mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Delete patient method controller")
    void deletePatientIfExists() throws Exception {

        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(3L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(7L, "Juan", "Perez", "87609843", date, address);

        Long id = 7L;

        when(patientRepository.findById(id)).thenReturn(Optional.of(patientEntity));
        //doNothing().when(patientRepository).delete(patientEntity);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.delete("/clinical-dental/patients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted patient"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                        .andReturn();

        Assertions.assertEquals("application/json" , mvcResult.getResponse().getContentType());


    }

    @Test
    @DisplayName("Delete patient method controller - Throws exception if patient does not exists")
    void deleteMethodThrowsResourceNotFoundException() throws Exception {

        Long id = 7L;

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.delete("/clinical-dental/patients/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The patient does not exists"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                        .andReturn();

        Assertions.assertEquals("application/json" , mvcResult.getResponse().getContentType());


    }

    @Test
    @DisplayName("Get All patients if exists - method controller")
    void getAllPatientsMethodReturnsOk() throws Exception {

        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(7L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(5L, "Juan", "Perez", "8765439", date, address);

        List<Patient> patientList = List.of(patientEntity);

        when(patientRepository.findAll()).thenReturn(patientList);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.get("/clinical-dental/patients"))

                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(jsonPath("$", hasSize(patientList.size())))
                        .andReturn();

        Assertions.assertEquals("application/json" , mvcResult.getResponse().getContentType());
    }

}
