package au.com.telstra.simcardactivator;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/activateSimCard")
public class SimCardActivationController {

	private static final String FAILED_STATUS = "Activation Failed";
	private static final String ENDPOINT_URL = "http://localhost:8444/actuate";

	private final Logger logger = LoggerFactory.getLogger(SimCardActivationController.class);

	@Autowired
	SimCardRepository simCardRepository;

	@GetMapping("/{id}")
	public ResponseEntity<SimCard> getActivationStatusById(@PathVariable Long id) {
		Optional<SimCard> simCardOptional = simCardRepository.findById(id);
		if (simCardOptional.isPresent()) {
			return ResponseEntity.ok(simCardOptional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<String> activateSimCard(@RequestBody SimCard newSimCard) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ActivationResponseResult activationResult = new ActivationResponseResult();
			RestTemplate restTemplate = new RestTemplate();

			String iccid = newSimCard.getIccid();
			String customerEmail = newSimCard.getCustomerEmail();
			String requestBody = objectMapper.writeValueAsString(new SimCard(iccid, customerEmail));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT_URL, requestEntity, String.class);

			activationResult = objectMapper.readValue(response.getBody(),
					ActivationResponseResult.class);

			SimCard simCard = simCardRepository.save(new SimCard(iccid, customerEmail, activationResult.getSuccess()));

			return ResponseEntity.ok().body(objectMapper.writeValueAsString(simCard));
		} catch (JsonProcessingException e) {
			logger.error("Error processing JSON: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FAILED_STATUS);
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FAILED_STATUS);
		}
	}
}
