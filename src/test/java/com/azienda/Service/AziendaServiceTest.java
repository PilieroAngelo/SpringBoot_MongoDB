package com.azienda.Service;

import com.azienda.DTO.AziendaRequestDTO;
import com.azienda.DTO.AziendaRequestPatchDTO;
import com.azienda.DTO.AziendaResponseDTO;
import com.azienda.Entity.Azienda;
import com.azienda.Exceptions.IdNotFoundException;
import com.azienda.Mapper.AziendaMapper;
import com.azienda.Repository.AziendaRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AziendaServiceTest {

    @Mock
    private AziendaRepo aziendaRepo;

    @Mock
    private AziendaMapper aziendaMapper;

    @InjectMocks
    private AziendaService aziendaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAll_Success() {
        Azienda azienda = new Azienda("1", 23.3, "PPP", 2, "AA");
        List<Azienda> aziendaList = List.of(azienda);
        List<AziendaResponseDTO> responseList = List.of(new AziendaResponseDTO("1", 23.3, "PPP", 2, "AA"));

        when(aziendaRepo.findAll()).thenReturn(aziendaList);
        when(aziendaMapper.toList(aziendaList)).thenReturn(responseList);

        List<AziendaResponseDTO> result = aziendaService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(aziendaRepo, times(1)).findAll();
        verify(aziendaMapper, times(1)).toList(aziendaList);
    }

    @Test
    void insert_Success() {
        AziendaRequestDTO request = new AziendaRequestDTO(23.5, "PEEJ", 2, "ANNA");
        Azienda azienda = new Azienda("1", 23.5, "PEEJ", 2, "ANNA");
        AziendaResponseDTO responseDTO = new AziendaResponseDTO("1", 23.5, "PEEJ", 2, "ANNA");

        when(aziendaMapper.toEntity(request)).thenReturn(azienda);
        when(aziendaRepo.save(azienda)).thenReturn(azienda);
        when(aziendaMapper.entityToResponseDTO(azienda)).thenReturn(responseDTO);

        AziendaResponseDTO result = aziendaService.insert(request);

        assertNotNull(result);
        assertEquals("ANNA", result.getNomeAzienda());
        verify(aziendaRepo, times(1)).save(azienda);
        verify(aziendaMapper, times(1)).toEntity(request);
        verify(aziendaMapper, times(1)).entityToResponseDTO(azienda);
    }

    @Test
    void update_Success() {
        String id = "1";
        AziendaRequestDTO request = new AziendaRequestDTO(30.0, "NEWPIVA", 5, "Nuova Azienda");
        Azienda azienda = new Azienda(id, 25.0, "OLDPIVA", 3, "Vecchia Azienda");

        when(aziendaRepo.findById(id)).thenReturn(Optional.of(azienda));
        doNothing().when(aziendaMapper).updateEntityFromDTO(request, azienda);
        when(aziendaRepo.save(azienda)).thenReturn(azienda);
        when(aziendaMapper.entityToResponseDTO(azienda)).thenReturn(new AziendaResponseDTO(id, 30.0, "NEWPIVA", 5, "Nuova Azienda"));

        AziendaResponseDTO result = aziendaService.update(id, request);

        assertNotNull(result);
        assertEquals("Nuova Azienda", result.getNomeAzienda());
        verify(aziendaRepo, times(1)).findById(id);
        verify(aziendaMapper, times(1)).updateEntityFromDTO(request, azienda);
        verify(aziendaRepo, times(1)).save(azienda);
    }

    @Test
    void updateParziale_Success() {
        String id = "1";
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO("Nuovo Nome", 10, 50000.0);
        Azienda azienda = new Azienda(id, 20000.0, "PIVA123", 5, "Vecchio Nome");

        when(aziendaRepo.findById(id)).thenReturn(Optional.of(azienda));
        doNothing().when(aziendaMapper).updateEntityFromPatchDTO(patchDTO, azienda);
        when(aziendaRepo.save(azienda)).thenReturn(azienda);
        when(aziendaMapper.entityToResponseDTO(azienda)).thenReturn(new AziendaResponseDTO(id, 50000.0, "PIVA123", 10, "Nuovo Nome"));

        AziendaResponseDTO result = aziendaService.updateParziale(id, patchDTO);

        assertNotNull(result);
        assertEquals("Nuovo Nome", result.getNomeAzienda());
        verify(aziendaRepo, times(1)).findById(id);
        verify(aziendaMapper, times(1)).updateEntityFromPatchDTO(patchDTO, azienda);
        verify(aziendaRepo, times(1)).save(azienda);
    }

    @Test
    void delete_Success() {
        String id = "1";

        when(aziendaRepo.existsById(id)).thenReturn(true);
        doNothing().when(aziendaRepo).deleteById(id);

        aziendaService.delete(id);

        verify(aziendaRepo, times(1)).existsById(id);
        verify(aziendaRepo, times(1)).deleteById(id);
    }

    @Test
    void getByID_Success() {
        String id = "1";
        Azienda azienda = new Azienda(id, 23000.0, "PIVA456", 15, "AziendaTest");
        AziendaResponseDTO responseDTO = new AziendaResponseDTO(id, 23000.0, "PIVA456", 15, "AziendaTest");

        when(aziendaRepo.findById(id)).thenReturn(Optional.of(azienda));
        when(aziendaMapper.entityToResponseDTO(azienda)).thenReturn(responseDTO);

        AziendaResponseDTO result = aziendaService.getByID(id);

        assertNotNull(result);
        assertEquals("AziendaTest", result.getNomeAzienda());
        verify(aziendaRepo, times(1)).findById(id);
        verify(aziendaMapper, times(1)).entityToResponseDTO(azienda);
    }

    @Test
    void update_ThrowsIdNotFoundException() {
        String id = "99";
        AziendaRequestDTO requestDTO = new AziendaRequestDTO(30.0, "NEWPIVA", 10, "Nuova Azienda");

        // Simula il comportamento del repository che non trova l'ID
        when(aziendaRepo.findById(id)).thenReturn(Optional.empty());

        // Verifica che venga lanciata l'eccezione gestita da GlobalExceptionHandler
        assertThrows(IdNotFoundException.class, () -> aziendaService.update(id, requestDTO));

        // Verifica che il repository sia stato chiamato una sola volta
        verify(aziendaRepo, times(1)).findById(id);
    }

    @Test
    void getByID_ThrowsIdNotFoundException() {
        String id = "99";

        // Simuliamo il comportamento del repository: se cerchiamo un ID non esistente, restituiamo un Optional vuoto
        when(aziendaRepo.findById(id)).thenReturn(Optional.empty());

        // Verifica che il metodo del service lanci effettivamente l'eccezione
        assertThrows(IdNotFoundException.class, () -> aziendaService.getByID(id));

        // Verifica che il repository sia stato interrogato una sola volta con l'ID corretto
        verify(aziendaRepo, times(1)).findById(id);
    }

    @Test
    void updateParziale_ThrowsIdNotFoundException() {
        String id = "99";
        AziendaRequestPatchDTO patchDTO = new AziendaRequestPatchDTO("Nuovo Nome", 10, 50000.0);

        when(aziendaRepo.findById(id)).thenReturn(Optional.empty());

        // Assert that the exception is thrown and handled by GlobalExceptionHandler
        assertThrows(IdNotFoundException.class, () -> aziendaService.updateParziale(id, patchDTO));
        verify(aziendaRepo, times(1)).findById(id);
    }

    @Test
    void delete_ThrowsIdNotFoundException() {
        String id = "99";

        when(aziendaRepo.existsById(id)).thenReturn(false);

        // Assert that the exception is thrown and handled by GlobalExceptionHandler
        assertThrows(IdNotFoundException.class, () -> aziendaService.delete(id));
        verify(aziendaRepo, times(1)).existsById(id);
    }

}
