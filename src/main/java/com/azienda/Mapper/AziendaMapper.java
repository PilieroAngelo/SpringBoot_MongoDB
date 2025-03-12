package com.azienda.Mapper;

import com.azienda.DTO.AziendaRequestDTO;
import com.azienda.DTO.AziendaRequestPatchDTO;
import com.azienda.DTO.AziendaResponseDTO;
import com.azienda.Entity.Azienda;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AziendaMapper {

    public AziendaMapper() {

    }
//usa builder
public Azienda toEntity(AziendaRequestDTO aziendaRequestDTO) {
    return new Azienda.Builder()
            .nomeAzienda(aziendaRequestDTO.getNomeAzienda())
            .numeroPersonale(aziendaRequestDTO.getNumeroPersonale())
            .PIVA(aziendaRequestDTO.getPIVA())
            .nettoAnnuale(aziendaRequestDTO.getNettoAnnuale())
            .build();
}

    public void updateEntityFromDTO(AziendaRequestDTO dto, Azienda entity) {
            entity.setNomeAzienda(dto.getNomeAzienda());
            entity.setNumeroPersonale(dto.getNumeroPersonale());
            entity.setNettoAnnuale(dto.getNettoAnnuale());
    }

    public AziendaResponseDTO entityToResponseDTO(Azienda azienda) {
        AziendaResponseDTO aziendaResponseDTO = new AziendaResponseDTO();
        aziendaResponseDTO.setId(azienda.getId());
        aziendaResponseDTO.setNomeAzienda(azienda.getNomeAzienda());
        aziendaResponseDTO.setNumeroPersonale(azienda.getNumeroPersonale());
        aziendaResponseDTO.setPIVA(azienda.getPIVA());
        aziendaResponseDTO.setNettoAnnuale(azienda.getNettoAnnuale());
        return aziendaResponseDTO;
    }


    public void updateEntityFromPatchDTO(AziendaRequestPatchDTO patchDTO, Azienda entity) {
        if (patchDTO.getNomeAzienda() != null) {
            entity.setNomeAzienda(patchDTO.getNomeAzienda());
        }
        if (patchDTO.getNumeroPersonale() != 0) {
            entity.setNumeroPersonale(patchDTO.getNumeroPersonale());
        }
        if (patchDTO.getNettoAnnuale() != 0) {
            entity.setNettoAnnuale(patchDTO.getNettoAnnuale());
        }
    }

    public List<AziendaResponseDTO> toList(List<Azienda> azienda) {
        return azienda.stream().map(this::entityToResponseDTO).toList();
    }


}
