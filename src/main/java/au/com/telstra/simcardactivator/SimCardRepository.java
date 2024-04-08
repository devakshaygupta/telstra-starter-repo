package au.com.telstra.simcardactivator;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface SimCardRepository extends CrudRepository<SimCard, Long> {

  Optional<SimCard> findById(Long id);

}