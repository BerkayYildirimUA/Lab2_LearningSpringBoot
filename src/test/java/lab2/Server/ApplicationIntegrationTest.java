package lab2.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testPersonAndAccountWorkflow() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Create Person
        ResponseEntity<String> createPersonResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/persons/?name=John Doe", null, String.class);
        assertThat(createPersonResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        long personId = extractPersonId(createPersonResponse);

        // Create Account For John Doe
        ResponseEntity<String> createAccountResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts/?personId=" + personId, null, String.class);
        assertThat(createAccountResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        long accountId = extractAccountId(createAccountResponse);

        // Deposit into Account
        ResponseEntity<String> depositResponse = this.restTemplate.exchange("http://localhost:" + port + "/accounts/" + accountId + "/deposits?personId=" + personId + "&amount=1000", HttpMethod.PUT, request, String.class);
        assertThat(depositResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);

        // Withdraw from Account
        ResponseEntity<String> withdrawResponse = this.restTemplate.exchange("http://localhost:" + port + "/accounts/" + accountId + "/withdrawals?personId=" + personId + "&amount=200", HttpMethod.DELETE, request, String.class);
        assertThat(withdrawResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);

        // Check Balance
        ResponseEntity<String> balanceResponse = this.restTemplate.getForEntity("http://localhost:" + port + "/accounts/" + accountId + "/balance?personId=" + personId, String.class);
        assertThat(balanceResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        assertThat(balanceResponse.getBody()).contains("800.0");
    }

    @Test
    public void testDualOwnership() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Create Person1
        ResponseEntity<String> createPerson1Response = this.restTemplate.postForEntity("http://localhost:" + port + "/persons/?name=Person1", request, String.class);
        assertThat(createPerson1Response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        long person1Id = extractPersonId(createPerson1Response);

        // Create Person2
        ResponseEntity<String> createPerson2Response = this.restTemplate.postForEntity("http://localhost:" + port + "/persons/?name=Person2", request, String.class);
        assertThat(createPerson2Response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        long person2Id = extractPersonId(createPerson2Response);

        // Create Account For Person1
        ResponseEntity<String> createAccountResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts/?personId=" + person1Id, request, String.class);
        assertThat(createAccountResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        long accountId = extractAccountId(createAccountResponse);

        // Add Person2 to Account of person1
        ResponseEntity<String> addPersonResponse = this.restTemplate.exchange("http://localhost:" + port + "/accounts/" + accountId + "/person?personId=" + person2Id, HttpMethod.PUT, request, String.class);
        assertThat(addPersonResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);

        // Deposit into Account via Person1
        ResponseEntity<String> depositResponse = this.restTemplate.exchange("http://localhost:" + port + "/accounts/" + accountId + "/deposits?personId=" + person1Id + "&amount=1000", HttpMethod.PUT, request, String.class);
        assertThat(depositResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);

        // Withdraw from Account via Person2
        ResponseEntity<String> withdrawResponse = this.restTemplate.exchange("http://localhost:" + port + "/accounts/" + accountId + "/withdrawals?personId=" + person2Id + "&amount=200", HttpMethod.DELETE, request, String.class);
        System.out.println("http://localhost:" + port + "/accounts/withdraw?personId=" + person2Id + "&accountId=" + accountId + "&amount=200");
        assertThat(withdrawResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);

        // Create Account For Person3
        ResponseEntity<String> createPerson3Response = this.restTemplate.postForEntity("http://localhost:" + port + "/persons/?name=Person3", request, String.class);
        assertThat(createPerson3Response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        long person3Id = extractPersonId(createPerson3Response);

        // Deposit into Account via Person3 (who can't do that)
        ResponseEntity<String> deposit3Response = this.restTemplate.exchange("http://localhost:" + port + "/accounts/" + accountId + "/deposits?personId=" + person3Id + "&amount=1000", HttpMethod.PUT, request, String.class);
        assertThat(deposit3Response.getStatusCode().isError()).isEqualTo(true);

        // Check Balance via Person1
        ResponseEntity<String> balanceResponse = this.restTemplate.getForEntity("http://localhost:" + port + "/accounts/" + accountId + "/balance?personId=" + person1Id, String.class);
        assertThat(balanceResponse.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        assertThat(balanceResponse.getBody()).contains("800.0");

        // Check Balance via Person2
        ResponseEntity<String> balance2Response = this.restTemplate.getForEntity("http://localhost:" + port + "/accounts/" + accountId + "/balance?personId=" + person2Id, String.class);
        assertThat(balance2Response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        assertThat(balance2Response.getBody()).contains("800.0");
    }

    private Integer extractPersonId(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue(response.getBody(), Map.class);
            return (Integer) map.get("personID");
        } catch (Exception e) {
            fail("couldn't read message");
            return -1;
        }
    }

    private Integer extractAccountId(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue(response.getBody(), Map.class);
            return (Integer) map.get("accountID");
        } catch (Exception e) {
            fail("couldn't read message");
            return -1;
        }
    }
}
