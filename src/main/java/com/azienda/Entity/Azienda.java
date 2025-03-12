package com.azienda.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "aziende")
public class Azienda {
    @Id
    private String id;
    private String nomeAzienda;
    private int numeroPersonale;
    private String PIVA;
    private double nettoAnnuale;

    public Azienda() {

    }

    public Azienda(String id, double nettoAnnuale, String PIVA, int numeroPersonale, String nomeAzienda) {
        this.id = id;
        this.nettoAnnuale = nettoAnnuale;
        this.PIVA = PIVA;
        this.numeroPersonale = numeroPersonale;
        this.nomeAzienda = nomeAzienda;
    }



    public static class Builder {
        private String nomeAzienda;
        private int numeroPersonale;
        private String PIVA;
        private double nettoAnnuale;

        public Builder nomeAzienda(String nomeAzienda) {
            this.nomeAzienda = nomeAzienda;
            return this;
        }

        public Builder numeroPersonale(int numeroPersonale) {
            this.numeroPersonale = numeroPersonale;
            return this;
        }

        public Builder PIVA(String PIVA) {
            this.PIVA = PIVA;
            return this;
        }

        public Builder nettoAnnuale(double nettoAnnuale) {
            this.nettoAnnuale = nettoAnnuale;
            return this;
        }

        public Azienda build() {
            Azienda azienda = new Azienda();
            azienda.nomeAzienda = this.nomeAzienda;
            azienda.numeroPersonale = this.numeroPersonale;
            azienda.PIVA = this.PIVA;
            azienda.nettoAnnuale = this.nettoAnnuale;
            return azienda;
        }
    }
}
