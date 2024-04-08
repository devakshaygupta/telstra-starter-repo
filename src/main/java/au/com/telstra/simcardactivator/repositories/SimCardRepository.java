package au.com.telstra.simcardactivator.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import au.com.telstra.simcardactivator.models.SimCard;

public interface SimCardRepository extends CrudRepository<SimCard, Long> {

  Optional<SimCard> findById(Long id);

}