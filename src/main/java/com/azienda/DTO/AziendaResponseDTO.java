package com.azienda.DTO;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

//server al client
@Getter
@Setter
@EqualsAndHashCode
public class AziendaResponseDTO {
    private String id;
    private String nomeAzienda;
    private int numeroPersonale;
    private double nettoAnnuale;
    private String PIVA;

    public AziendaResponseDTO() {
    }

    public AziendaResponseDTO(String id, double nettoAnnuale, String PIVA, int numeroPersonale, String nomeAzienda) {
        this.id = id;
        this.nettoAnnuale = nettoAnnuale;
        this.PIVA = PIVA;
        this.numeroPersonale = numeroPersonale;
        this.nomeAzienda = nomeAzienda;
    }
}
