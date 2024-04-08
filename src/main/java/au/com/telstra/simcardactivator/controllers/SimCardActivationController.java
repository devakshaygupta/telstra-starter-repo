package au.com.telstra.simcardactivator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.com.telstra.simcardactivator.models.SimCard;
import au.com.telstra.simcardactivator.repositories.SimCardRepository;

@RestController
@RequestMapping("/api/activateSimCard")
public class SimCardActivationController {

	SimCardActivationHandler activationHandler = new SimCardActivationHandler();

	@Autowired
	SimCardRepository simCardRepository;

	@GetMapping("/{id}")
	public ResponseEntity<SimCard> getStatusById(@PathVariable Long id) {
		return activationHandler.getActivationStatus(id, simCardRepository);
	}

	@PostMapping
	public ResponseEntity<String> activateSimCard(@RequestBody SimCard newSimCard) {
		return activationHandler.handleSimCardActivation(newSimCard, simCardRepository);
	}
}