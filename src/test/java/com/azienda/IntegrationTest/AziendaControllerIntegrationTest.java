/*package com.azienda.IntegrationTest;

import com.azienda.DTO.AziendaRequestDTO;
import com.azienda.DTO.AziendaRequestPatchDTO;
import com.azienda.DTO.AziendaResponseDTO;
import com.azienda.Entity.Azienda;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.azienda.Repository.AziendaRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;


import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AziendaControllerIntegrationTest {

    @Autowired
    private AziendaRepo aziendaRepository;

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Azienda aziendaTest;
    private AziendaRequestDTO request;
    private AziendaRequestPatchDTO patchDto;
    private String baseUrl;

    @BeforeEach
    void setup() {
        aziendaRepository.deleteAll(); // Pulizia DB per test isolati

        aziendaTest = new Azienda("1", 232, "394JE0I3323E", 2, "PPA");
        aziendaRepository.save(aziendaTest);

        request = new AziendaRequestDTO(23.3, "PSOE9384ON21", 2, "AAA");
        patchDto = new AziendaRequestPatchDTO("PPPP", 23, 3234);

        baseUrl = "http://localhost:" + port + "/aziende";

        given().port(port);  // Impostiamo la porta dinamica per ogni richiesta
    }

    @Test
    void insert() throws JsonProcessingException {
        String requestJson = objectMapper.writeValueAsString(request);

        // Usa RestAssured per inviare una POST request
        AziendaResponseDTO response = given()
                .contentType("application/json")
                .body(requestJson)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(AziendaResponseDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotEmpty();
    }

    @Test
    void getByID() {
        AziendaResponseDTO response = given()
                .when()
                .get(baseUrl +"/"+ aziendaTest.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(AziendaResponseDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.getNomeAzienda()).isEqualTo(aziendaTest.getNomeAzienda());
    }

    @Test
    void getByID_NotFound() {
        given()
                .when()
                .get(baseUrl +"/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getAll() {
        AziendaResponseDTO[] response = given()
                .when()
                .get(baseUrl)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(AziendaResponseDTO[].class);

        assertThat(response).isNotEmpty();
    }

    @Test
    void update() throws JsonProcessingException {
        String requestJson = objectMapper.writeValueAsString(request);

        // Usa RestAssured per inviare una PUT request
        given()
                .contentType("application/json")
                .body(requestJson)
                .when()
                .put(baseUrl+"/" + aziendaTest.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(org.hamcrest.Matchers.equalTo("Modifica Effettuata"));
    }

    @Test
    void patch_ValidInput() throws JsonProcessingException {
        String patchJson = objectMapper.writeValueAsString(patchDto);

        // Usa RestAssured per inviare una PATCH request
        given()
                .contentType("application/json")
                .body(patchJson)
                .when()
                .patch(baseUrl+"/" + aziendaTest.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(org.hamcrest.Matchers.equalTo("Modifica effettuata"));
    }

    @Test
    void delete() {
        given()
                .when()
                .delete(baseUrl+"/" + aziendaTest.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        Optional<Azienda> deleted = aziendaRepository.findById(aziendaTest.getId());
        assertThat(deleted).isEmpty();
    }
}*/
