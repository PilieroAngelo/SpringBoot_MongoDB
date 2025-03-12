package com.azienda.Repository;

import com.azienda.Entity.Azienda;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AziendaRepo extends MongoRepository<Azienda, String> {

}
