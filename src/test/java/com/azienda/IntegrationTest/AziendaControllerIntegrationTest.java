package com.azienda.IntegrationTest;

import com.azienda.DTO.AziendaRequestDTO;
import com.azienda.DTO.AziendaRequestPatchDTO;
import com.azienda.DTO.AziendaResponseDTO;
import com.azienda.Entity.Azienda;
import com.azienda.Repository.AziendaRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)  // Ogni test ha una nuova istanza della classe
class AziendaControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AziendaRepo aziendaRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Azienda aziendaTest;
    private AziendaRequestDTO request;
    private AziendaRequestPatchDTO patchDto;

    @BeforeEach
    void setup() {
        aziendaRepository.deleteAll(); // Pulizia DB per test isolati

        aziendaTest = new Azienda("1", 232, "394JE0I3323E", 2, "PPA");
        aziendaRepository.save(aziendaTest);

        request = new AziendaRequestDTO(23.3, "PSOE9384ON21", 2, "AAA");
        patchDto = new AziendaRequestPatchDTO("PPPP", 23, 3234);
    }

    @Test
    void insert() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = objectMapper.writeValueAsString(request);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<AziendaResponseDTO> response = restTemplate.postForEntity("/aziende", entity, AziendaResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotEmpty();
    }

    @Test
    void getByID() {
        ResponseEntity<AziendaResponseDTO> response = restTemplate.getForEntity("/aziende/" + aziendaTest.getId(), AziendaResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNomeAzienda()).isEqualTo(aziendaTest.getNomeAzienda());
    }

    @Test
    void getByID_NotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/aziende/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAll() {
        ResponseEntity<AziendaResponseDTO[]> response = restTemplate.getForEntity("/aziende", AziendaResponseDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void update() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = objectMapper.writeValueAsString(request);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/aziende/" + aziendaTest.getId(), HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Modifica Effettuata");
    }

    @Test
    void patch_ValidInput() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String patchJson = objectMapper.writeValueAsString(patchDto);
        HttpEntity<String> entity = new HttpEntity<>(patchJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/aziende/" + aziendaTest.getId(), HttpMethod.PATCH, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Modifica effettuata");
    }

    @Test
    void delete() {
        restTemplate.delete("/aziende/" + aziendaTest.getId());

        Optional<Azienda> deleted = aziendaRepository.findById(aziendaTest.getId());
        assertThat(deleted).isEmpty();
    }
}
