package lab2.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab2.Server.Entity.Person;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class BankClient {
    private static final String ACCOUNT_URL = "http://localhost:8081/accounts";
    private static final String PERSON_URL = "http://localhost:8081/persons";
    private final RestTemplate restTemplate = new RestTemplate();
    private long port = 8081;


    public Double getBalance(Long accountId) {
        String url = ACCOUNT_URL + "/balance/" + accountId;
        ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
        return response.getBody();
    }

    public void deposit(Long accountId, Double amount) {
        String url = ACCOUNT_URL + "/deposit";
    }

    public void createPerson(String name) {
        String url = PERSON_URL + "/create";

        Map<String, String> payload = new HashMap<>();
        payload.put("name", name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        restTemplate.postForObject(url, entity, Object.class);
    }


    public static void main(String[] args) {
        BankClient client = new BankClient();

        client.createPerson("Hello World");
    }
}