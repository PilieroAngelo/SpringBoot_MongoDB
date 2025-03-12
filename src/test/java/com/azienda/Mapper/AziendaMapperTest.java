package com.azienda.Mapper;

import com.azienda.DTO.AziendaRequestDTO;
import com.azienda.DTO.AziendaRequestPatchDTO;
import com.azienda.DTO.AziendaResponseDTO;
import com.azienda.Entity.Azienda;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AziendaMapperTest {


    private AziendaMapper aziendaMapper;
    private EasyRandom easyRandom;

    @BeforeAll
    public void setup() {
        aziendaMapper = new AziendaMapper();
        easyRandom = new EasyRandom();
    }

    @Test
    void testToEntity() {
        AziendaRequestDTO aziendaRequestDTO = new AziendaRequestDTO();
        aziendaRequestDTO.setNomeAzienda("Test Azienda");
        aziendaRequestDTO.setNumeroPersonale(100);
        aziendaRequestDTO.setPIVA("12345678901");
        aziendaRequestDTO.setNettoAnnuale(500000);

        Azienda azienda = aziendaMapper.toEntity(aziendaRequestDTO);

        assertNotNull(azienda);
        assertEquals("Test Azienda", azienda.getNomeAzienda());
        assertEquals(100, azienda.getNumeroPersonale());
        assertEquals("12345678901", azienda.getPIVA());
        assertEquals(500000, azienda.getNettoAnnuale());
    }

    @Test
    void testEntityToResponse() {
        Azienda azienda = new Azienda();
        AziendaRequestDTO requestDTO = new AziendaRequestDTO(23.5, "AP34ON230912", 3, "aa");
        azienda.setId("1");
        azienda.setNomeAzienda(requestDTO.getNomeAzienda());
        azienda.setNumeroPersonale(requestDTO.getNumeroPersonale());
        azienda.setNettoAnnuale(requestDTO.getNettoAnnuale());
        azienda.setPIVA(requestDTO.getPIVA());

        AziendaResponseDTO aziendaDto = aziendaMapper.entityToResponseDTO(azienda);

        assertNotNull(aziendaDto);
        assertNotNull(aziendaDto.getId());
        assertEquals(azienda.getPIVA(), aziendaDto.getPIVA());
        assertEquals(azienda.getNomeAzienda(), aziendaDto.getNomeAzienda());
    }

    @Test
    void testUpdateEntityFromDTO() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestDTO aziendaDto = easyRandom.nextObject(AziendaRequestDTO.class);

        existingAzienda.setId("1");

        aziendaMapper.updateEntityFromDTO(aziendaDto, existingAzienda);

        assertNotNull(existingAzienda.getId());
        assertNotNull(existingAzienda.getNomeAzienda());
        assertNotNull(existingAzienda.getPIVA());

        assertNotEquals(aziendaDto.getPIVA(), existingAzienda.getPIVA());
    }

    @Test
    void testUpdateEntityFromPatchDTO() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = easyRandom.nextObject(AziendaRequestPatchDTO.class);

        existingAzienda.setId("1");

        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertNotNull(existingAzienda.getId());
        assertNotNull(existingAzienda.getNomeAzienda());
        assertNotNull(existingAzienda.getPIVA());
    }

    @Test
    void testToList() {
        Azienda azienda1 = new Azienda("1", 23.4, "PAEI37402912", 3, "aa");
        Azienda azienda2 = new Azienda("2", 21.4, "PUENT2940382", 1, "BB");
        List<Azienda> aziendaList = List.of(azienda1, azienda2);

        List<AziendaResponseDTO> aziendaResponseDTOList = aziendaMapper.toList(aziendaList);

        assertNotNull(aziendaResponseDTOList);
        assertEquals(aziendaList.size(), aziendaResponseDTOList.size());
    }

    @Test
    void testToList_withEmptyList() {
        List<Azienda> emptyList = List.of();

        List<AziendaResponseDTO> responseDTOList = aziendaMapper.toList(emptyList);

        assertNotNull(responseDTOList);
        assertEquals(0, responseDTOList.size());
    }

    @Test
    void testUpdateEntityFromPatchDTO_nomeAziendaNotNull() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO();
        patchDTO.setNomeAzienda("Updated Azienda");

        existingAzienda.setId("1");
        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertNotNull(existingAzienda.getNomeAzienda());
        assertEquals("Updated Azienda", existingAzienda.getNomeAzienda());
    }

    @Test
    void testUpdateEntityFromPatchDTO_numeroPersonaleNotZero() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO();
        patchDTO.setNumeroPersonale(100);

        existingAzienda.setId("1");
        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertEquals(100, existingAzienda.getNumeroPersonale());
    }

    @Test
    void testUpdateEntityFromPatchDTO_nettoAnnualeNotZero() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO();
        patchDTO.setNettoAnnuale(50000);

        existingAzienda.setId("1");
        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertEquals(50000, existingAzienda.getNettoAnnuale());
    }

    @Test
    void testUpdateEntityFromPatchDTO_nomeAziendaNull() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO();
        patchDTO.setNomeAzienda(null); // Verifica che il nome azienda non venga aggiornato

        existingAzienda.setId("1");
        String oldNomeAzienda = existingAzienda.getNomeAzienda();
        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertEquals(oldNomeAzienda, existingAzienda.getNomeAzienda()); // Nome azienda dovrebbe rimanere invariato
    }

    @Test
    void testUpdateEntityFromPatchDTO_numeroPersonaleZero() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO();
        patchDTO.setNumeroPersonale(0); // Verifica che numero personale non venga aggiornato se è 0

        existingAzienda.setId("1");
        int oldNumeroPersonale = existingAzienda.getNumeroPersonale();
        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertEquals(oldNumeroPersonale, existingAzienda.getNumeroPersonale()); // Numero personale dovrebbe rimanere invariato
    }

    @Test
    void testUpdateEntityFromPatchDTO_nettoAnnualeZero() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO();
        patchDTO.setNettoAnnuale(0); // Verifica che netto annuale non venga aggiornato se è 0

        existingAzienda.setId("1");
        double oldNettoAnnuale = existingAzienda.getNettoAnnuale();
        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertEquals(oldNettoAnnuale, existingAzienda.getNettoAnnuale()); // Netto annuale dovrebbe rimanere invariato
    }

    @Test
    void testUpdateEntityFromPatchDTO_allFieldsNotNull() {
        Azienda existingAzienda = easyRandom.nextObject(Azienda.class);
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO();
        patchDTO.setNomeAzienda("Updated Azienda");
        patchDTO.setNumeroPersonale(100);
        patchDTO.setNettoAnnuale(50000);

        existingAzienda.setId("1");
        aziendaMapper.updateEntityFromPatchDTO(patchDTO, existingAzienda);

        assertEquals("Updated Azienda", existingAzienda.getNomeAzienda());
        assertEquals(100, existingAzienda.getNumeroPersonale());
        assertEquals(50000, existingAzienda.getNettoAnnuale());
    }
}
