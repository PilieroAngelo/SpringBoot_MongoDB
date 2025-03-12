package com.azienda.DTO;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class AziendaRequestPatchDTO {
    private String nomeAzienda;

    @Min(value = 1, message = "Il personale non può essere inferiore ad 1")
    private int numeroPersonale;

    @Min(value = 0, message = "Il netto annuo non può essere inferiore a 0")
    private double nettoAnnuale;

    public AziendaRequestPatchDTO() {
    }

    public AziendaRequestPatchDTO(String nomeAzienda, int numeroPersonale, double nettoAnnuale) {
        this.nomeAzienda = nomeAzienda;
        this.numeroPersonale = numeroPersonale;
        this.nettoAnnuale = nettoAnnuale;
    }
}
