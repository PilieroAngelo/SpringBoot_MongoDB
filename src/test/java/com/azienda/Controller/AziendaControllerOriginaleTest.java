/*package com.azienda.Controller;

import com.azienda.DTO.AziendaRequestDTO;
import com.azienda.DTO.AziendaRequestPatchDTO;
import com.azienda.DTO.AziendaResponseDTO;
import com.azienda.Entity.Azienda;
import com.azienda.Exceptions.IdNotFoundException;
import com.azienda.Service.AziendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AziendaController.class)
class AziendaControllerOriginaleTest {

    private ObjectMapper objectMapper;

    private AziendaRequestDTO request, badRequest;
    private Azienda aziendaTest;
    private AziendaRequestPatchDTO patchDto, badPatch;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();

        aziendaTest = new Azienda("1", 232, "394JE0I3323E", 2, "PPA");

        badRequest = new AziendaRequestDTO(-2, "PSN21", 0, "");

        request = new AziendaRequestDTO(23.3, "PSOE9384ON21", 2, "AAA");

        patchDto = new AziendaRequestPatchDTO("PPPP", 23, 3234);

        badPatch = new AziendaRequestPatchDTO("", -2, -12);
    }

    @MockitoBean
    private AziendaService aziendaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void insert() throws Exception {
        AziendaResponseDTO responseDTO = new AziendaResponseDTO("1", request.getNettoAnnuale(), request.getPIVA(), request.getNumeroPersonale(), request.getNomeAzienda());

        when(aziendaService.insert(request)).thenReturn(responseDTO);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/aziende")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value("1"));

        System.out.println("Response DTO: " + responseDTO);
        verify(aziendaService, times(1)).insert(request);
    }

    @Test
    void getByID() throws Exception {
        AziendaResponseDTO responseDTO = new AziendaResponseDTO("1", 23.4, "PP", 3, "AA");
        Azienda existingAzienda = new Azienda("1", 23.3, "PINEO3482018", 2, "aa");

        when(aziendaService.getByID(existingAzienda.getId())).thenReturn(responseDTO);

        mockMvc.perform(get("/aziende/" + aziendaTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nettoAnnuale").value(23.4))
                .andExpect(jsonPath("$.piva").value("PP"))
                .andExpect(jsonPath("$.numeroPersonale").value(3))
                .andExpect(jsonPath("$.nomeAzienda").value("AA"));

        verify(aziendaService, times(1)).getByID(aziendaTest.getId());
    }

    @Test
    void getAll() throws Exception {
        AziendaResponseDTO responseDTO1 = new AziendaResponseDTO("1", 23.4, "PP", 3, "AA");
        AziendaResponseDTO responseDTO2 = new AziendaResponseDTO("2", 25.6, "RR", 5, "BB");
        List<AziendaResponseDTO> aziendaList = List.of(responseDTO1, responseDTO2);

        when(aziendaService.getAll()).thenReturn(aziendaList);

        mockMvc.perform(get("/aziende"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));

        verify(aziendaService, times(1)).getAll();
    }

    @Test
    void update() throws Exception {
        AziendaResponseDTO updatedAzienda = new AziendaResponseDTO(aziendaTest.getId(), request.getNettoAnnuale(), request.getPIVA(), request.getNumeroPersonale(), request.getNomeAzienda());
        String requestJson = objectMapper.writeValueAsString(request);

        when(aziendaService.update(eq("1"), any(AziendaRequestDTO.class))).thenReturn(updatedAzienda);

        mockMvc.perform(put("/aziende/" + aziendaTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Modifica Effettuata"));

        verify(aziendaService, times(1)).update(aziendaTest.getId(), request);
    }

    @Test
    void update_InvalidInput() throws Exception {
        String badRequestJson = objectMapper.writeValueAsString(badRequest);

        when(aziendaService.update(aziendaTest.getId(), badRequest)).thenThrow(MethodArgumentTypeMismatchException.class);

        mockMvc.perform(put("/aziende/" + aziendaTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());

        verify(aziendaService, times(0)).update(any(), any());
    }

    @Test
    void patch_InvalidInput() throws Exception {

        when(aziendaService.updateParziale(aziendaTest.getId(), badPatch)).thenThrow(MethodArgumentTypeMismatchException.class);

        String patchInvalid = objectMapper.writeValueAsString(badPatch);

        mockMvc.perform(put("/aziende/" + aziendaTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchInvalid))
                .andExpect(status().isBadRequest());

        verify(aziendaService, times(0)).updateParziale(any(), any());
    }

    @Test
    void patch_ValidInput() throws Exception {
        AziendaResponseDTO updatedAzienda = new AziendaResponseDTO(aziendaTest.getId(), patchDto.getNettoAnnuale(), aziendaTest.getPIVA(), patchDto.getNumeroPersonale(), patchDto.getNomeAzienda());
        String patchJson = objectMapper.writeValueAsString(patchDto);

        when(aziendaService.updateParziale(aziendaTest.getId(), patchDto)).thenReturn(updatedAzienda);

        mockMvc.perform(patch("/aziende/" + aziendaTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Modifica effettuata"));

        verify(aziendaService, times(1)).updateParziale(aziendaTest.getId(), patchDto);
    }

    @Test
    void getByID_NotFound() throws Exception {
        String id = "non_esistente";
        when(aziendaService.getByID(id)).thenThrow(IdNotFoundException.class);

        mockMvc.perform(get("/aziende/" + id))
                .andExpect(status().isNotFound());
        verify(aziendaService, times(1)).getByID(id);
    }

    @Test
    void delete() throws Exception {
        String id = "1";

        mockMvc.perform(MockMvcRequestBuilders.delete("/aziende/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminazione Effettuata"));

        verify(aziendaService, times(1)).delete(id);
    }

    @Test
    void insert_InvalidInput() throws Exception {

        mockMvc.perform(post("/aziende")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nettoAnnuale\": -23.5, \"piva\": \"\", \"numeroPersonale\": 0, \"nomeAzienda\": \"\" }"))
                .andExpect(status().isBadRequest());

        verify(aziendaService, times(0)).insert(any());
    }

    @Test
    void testGenericException() throws Exception {
        when(aziendaService.getAll()).thenThrow(new RuntimeException("Generic Exception"));

        mockMvc.perform(get("/aziende"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Generic Exception"));
    }
}*/
