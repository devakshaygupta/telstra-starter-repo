package au.com.telstra.simcardactivator;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimCardActivationHandler {

    private static final String FAILED_STATUS = "Activation Failed";
    private static final String ENDPOINT_URL = "http://localhost:8444/actuate";

    private final Logger logger = LoggerFactory.getLogger(SimCardActivationHandler.class);

    public ResponseEntity<String> handleSimCardActivation(SimCard newSimCard, SimCardRepository simCardRepository) {
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

            SimCard simCardToActivate = new SimCard(iccid, customerEmail, activationResult.getSuccess());

            simCardRepository.save(simCardToActivate);

            return ResponseEntity.ok().body(objectMapper.writeValueAsString(simCardToActivate));
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FAILED_STATUS);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FAILED_STATUS);
        }
    }

    public ResponseEntity<SimCard> getActivationStatus(Long id, SimCardRepository simCardRepository) {
        try {
            Optional<SimCard> simCardOptional = simCardRepository.findById(id);
            if (simCardOptional.isPresent()) {
                return ResponseEntity.ok(simCardOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimCard());
        }
    }

}