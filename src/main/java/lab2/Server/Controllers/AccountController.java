package lab2.Server.Controllers;

import lab2.Server.Entity.Account;
import lab2.Server.Services.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private final Bank bank;

    public AccountController(Bank bank){
        this.bank = bank;
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalnce(@RequestBody Map<String, Object> payload){
        long id = Long.parseLong(payload.get("id").toString());

        Optional<Double> accountOptional = bank.getBalance(id);
        if (accountOptional.isPresent()){
            return ResponseEntity.ok(accountOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> addBalance(@RequestBody Map<String, Object> payload) {
        long id = Long.parseLong(payload.get("id").toString());
        double amount = Double.parseDouble(payload.get("amount").toString());

        if (bank.deposit(id, amount)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("{\"error\": \"Deposit failed\"}");
    }


    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdrawBalance(@RequestBody Map<String, Object> payload) {
        long id = Long.parseLong(payload.get("id").toString());
        double amount = Double.parseDouble(payload.get("amount").toString());

        Optional<Double> withdrawl = bank.withdraw(id, amount);
        if(withdrawl.isPresent()) {
            return ResponseEntity.ok().body("here is your €" + withdrawl.get().toString());
        }
        return ResponseEntity.badRequest().body("{\"error\": \"Withdrawal failed\"}");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeBalance(@RequestBody Map<String, Object> payload) {
        long id = Long.parseLong(payload.get("id").toString());
        double amount = Double.parseDouble(payload.get("amount").toString());

        Optional<Double> withdrawl = bank.withdraw(id, amount);
        if(withdrawl.isPresent()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("{\"error\": \"Remove failed\"}");
    }

    @PostMapping("/createAcount")
    private ResponseEntity<?> createAcount(@RequestBody Map<String, Object> payload){
        long personId = Long.parseLong(payload.get("id").toString());
        Optional<Account> newAcount = bank.createAcount(personId);

        if (newAcount.isPresent()){
            return ResponseEntity.ok().body("Acount has been made");
        }
        return ResponseEntity.badRequest().body("{\"error\": \"account couldn't be made\"}");
    }

    @PutMapping("/addPersonToAccount")
    public ResponseEntity<?> addPersonToAccount(@RequestBody Map<String, Object> payload) {
        long personId = Long.parseLong(payload.get("personId").toString());
        long accountId = Long.parseLong(payload.get("accountId").toString());

        if (bank.addPersonToAccount(personId, accountId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("{\"error\": \"couldn't add person to account\"}");
    }

}
